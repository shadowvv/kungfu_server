package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.apache.ibatis.session.SqlSession;
import org.npc.kungfu.database.*;
import org.npc.kungfu.logic.message.ErrorCode;
import org.npc.kungfu.logic.message.ErrorMessage;
import org.npc.kungfu.logic.message.MessageEnum;
import org.npc.kungfu.logic.message.SSPlayerChannelReconnect;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.SimplePassenger;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;
import org.npc.kungfu.redis.JedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录服务
 */
public class LoginService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private static final String USER_NAME_PLAYER_IDS = "userNamePlayerIds";

    private static final LoginService service = new LoginService();

    private LoginService() {
    }

    /**
     * @return 登录服务单例
     */
    public static LoginService getService() {
        return service;
    }

    /**
     * 登录业务调度器
     */
    private BusStation<SoloPassengerBus<SimplePassenger<BaseMessage>, BaseMessage>, SimplePassenger<BaseMessage>, BaseMessage> taskStation;
    /**
     * 玩家id生成器
     */
    private AtomicInteger playerIdCreator;
    private volatile boolean nextPlayerIdChanged = false;
    /**
     * 使用玩家昵称作为登录的互斥量
     */
    private final ConcurrentHashMap<String, Boolean> userNameLocks = new ConcurrentHashMap<>();

    /**
     * 使用网络连接作为登录的互斥量
     */
    private final ConcurrentHashMap<Channel, Boolean> channelLocks = new ConcurrentHashMap<>();

    /**
     * 初始化
     *
     * @param station      调度器
     */
    public void init(BusStation<SoloPassengerBus<SimplePassenger<BaseMessage>, BaseMessage>, SimplePassenger<BaseMessage>, BaseMessage> station) {
        taskStation = station;

        logger.info("load nextPlayerId and load all userName to Redis");
        GameInfoEntity gameInfoEntity = null;
        List<String> userNames = new ArrayList<>();
        try (SqlSession session = MyBatisUtils.getSession()) {
            GameInfoMapper gameInfoMapper = session.getMapper(GameInfoMapper.class);
            gameInfoEntity = gameInfoMapper.getGameInfo();

            PlayerInfoMapper playerInfoMapper = session.getMapper(PlayerInfoMapper.class);
            userNames = playerInfoMapper.getAllPlayerUserNames();

            session.commit(); // 统一提交事务
        } catch (Exception e) {
            logger.error("Failed to get game info: {}", e.getMessage());
            return;
        }

        if (!JedisUtil.isConnectionOk()) {
            logger.error("Redis connection is not available.");
            return;
        }

        Map<String, String> userNameMap = new HashMap<>();
        for (String nickName : userNames) {
            userNameMap.put(nickName, "1");
        }
        JedisUtil.hmset(USER_NAME_PLAYER_IDS, userNameMap);


        playerIdCreator = new AtomicInteger(gameInfoEntity.getNextPlayerId());
        logger.info("LoginService initialized with taskStation and playerIdCreator:{}.", gameInfoEntity.getNextPlayerId());
    }

    /**
     * 投递消息
     * @param msg 消息
     */
    public void putMessage(BaseClientMessage msg) {
        taskStation.put(0, msg);
    }

    /**
     * 玩家断开连接
     *
     * @param channel 网络通道
     */
    public void onChannelInactive(Channel channel) {
        logger.debug("Channel {} is inactive.", channel);
        //TODO: Implement channel inactive handling
    }

    /**
     * 接收服务器消息
     *
     * @param serverMessage 服务器消息
     */
    public void putMessage(BaseServerMessage serverMessage) {

    }

    public void onPlayerRegister(Channel channel, String userName, String password) {
        logger.info("Processing player registration request for username: {}", userName);
        // 如果channel已经关闭，则不处理
        if (!channel.isActive()) {
            logger.warn("Channel is inactive, registration request for username: {} ignored", userName);
            return;
        }
        // 如果channel已经登录，则不处理
        if (PlayerService.getService().getPlayerId(channel) != 0) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.LOGIN_CHANNEL_BIND_PLAYER.getCode()));
            logger.warn("Channel is already bound to a player, registration request for username: {} ignored", userName);
            return;
        }
        // 如果用户名正在登录，则不处理
        Boolean userNameMutex = LoginService.getService().enterUserNameMutex(userName);
        if (userNameMutex != null) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            channel.close();
            logger.warn("Username: {} is already in use, registration request ignored", userName);
            return;
        }
        // 如果channel正在登录，则不处理
        Boolean channelMutex = LoginService.getService().enterChannelMutex(channel);
        if (channelMutex != null) {
            LoginService.getService().ExitMutex(userName);
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            channel.close();
            logger.warn("Channel is already in use for registration, request for username: {} ignored", userName);
            return;
        }
        // 如果用户名存在，则不处理
        if (LoginService.getService().checkUserNameExist(userName)) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(channel);
            logger.warn("Username: {} already exists, registration request ignored", userName);
            return;
        }
        // 如果用户名在线
        Player player = PlayerService.getService().getPlayer(userName);
        if (player != null) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(channel);
            logger.warn("Username: {} is already online, registration request ignored", userName);
            return;
        }

        // 创建玩家
        player = LoginService.getService().createPlayer(channel, userName, password);
        if (player == null) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.REGISTER_REQ.getId(), ErrorCode.SYSTEM_ERROR.getCode()));
            LoginService.getService().enterUserNameMutex(userName);
            LoginService.getService().ExitMutex(channel);
            channel.close();
            logger.error("Failed to create player for username: {}", userName);
            return;
        }
        // 登录成功
        LoginService.getService().onPlayerRegisterSuccess(player);
        player.sendRegisterSuccess();

        // 退出互斥
        LoginService.getService().ExitMutex(userName);
        LoginService.getService().ExitMutex(channel);
        logger.info("Player registration successful for username: {}", userName);
    }

    public void onPlayerLogin(Channel channel, String userName, String password) {
        logger.info("Processing player login request for username: {}", userName);
        // 如果channel已经关闭，则不处理
        if (!channel.isActive()) {
            logger.warn("Channel is inactive, login request for username: {} ignored", userName);
            return;
        }
        // 如果channel已经登录，则不处理
        if (PlayerService.getService().getPlayerId(channel) != 0) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.LOGIN_REQ.getId(), ErrorCode.LOGIN_CHANNEL_BIND_PLAYER.getCode()));
            logger.warn("Channel is already bound to a player, login request for username: {} ignored", userName);
            return;
        }
        // 如果用户名正在登录，则不处理
        Boolean userNameMutex = LoginService.getService().enterUserNameMutex(userName);
        if (userNameMutex != null) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.LOGIN_REQ.getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            channel.close();
            logger.warn("Username: {} is already in use, login request ignored", userName);
            return;
        }
        // 如果channel正在登录，则不处理
        Boolean channelMutex = LoginService.getService().enterChannelMutex(channel);
        if (channelMutex != null) {
            LoginService.getService().ExitMutex(userName);
            channel.writeAndFlush(new ErrorMessage(MessageEnum.LOGIN_REQ.getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            channel.close();
            logger.warn("Channel is already in use for login, request for username: {} ignored", userName);
            return;
        }
        // 如果用户名存在，则不处理
        if (!LoginService.getService().checkUserNameExist(userName)) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.LOGIN_REQ.getId(), ErrorCode.LOGIN_USERNAME_PASSWORD_ERROR.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(channel);
            logger.warn("Username: {} not exists, login request ignored", userName);
            return;
        }

        // 如果用户名在线
        Player player = PlayerService.getService().getPlayer(userName);
        if (player != null) {
            // 如果用户名在线，顶号
            if (player.getChannel().isActive()) {
                //TODO:顶号
            } else {
                // 如果用户名不在线，重新登录
                SSPlayerChannelReconnect ssPlayerChannelReconnect = new SSPlayerChannelReconnect(player.getId(), channel);
                PlayerService.getService().putMessage(ssPlayerChannelReconnect);
            }
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(channel);
            logger.info("Player reconnected for username: {}", userName);
            return;
        }

        // 创建玩家
        player = LoginService.getService().loadPlayer(channel, userName, password);
        if (player == null) {
            channel.writeAndFlush(new ErrorMessage(MessageEnum.LOGIN_REQ.getId(), ErrorCode.SYSTEM_ERROR.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(channel);
            channel.close();
            logger.error("Failed to load player for username: {}", userName);
            return;
        }
        // 登录成功
        LoginService.getService().onPlayerLoginSuccess(player);
        player.sendLoginSuccess();

        // 退出互斥
        LoginService.getService().ExitMutex(userName);
        LoginService.getService().ExitMutex(channel);
        logger.info("Player login successful for username: {}", userName);
    }

    /**
     * 进入互斥区
     * @param userName 用户昵称
     * @return 是否成功
     */
    private Boolean enterUserNameMutex(String userName) {
        return userNameLocks.putIfAbsent(userName, Boolean.TRUE);
    }

    /**
     * 退出互斥区
     * @param userName 用户昵称
     */
    private void ExitMutex(String userName) {
        userNameLocks.remove(userName);
    }

    /**
     * 进入互斥区
     * @param senderChannel 网络通道
     * @return 是否成功
     */
    private Boolean enterChannelMutex(Channel senderChannel) {
        return channelLocks.putIfAbsent(senderChannel,Boolean.TRUE);
    }

    /**
     * 退出互斥区
     * @param senderChannel 网络通道
     */
    private void ExitMutex(Channel senderChannel) {
        channelLocks.remove(senderChannel);
    }

    /**
     * 检测用户昵称是否可用
     * @param userName 用户昵称
     * @return 是否可用
     */
    private boolean checkUserNameExist(String userName) {
        logger.debug("Checking if username {} exists.", userName);
        try {
            if (!JedisUtil.isConnectionOk()) {
                logger.error("Redis connection is not available.");
                return false;
            }
            return JedisUtil.hget(USER_NAME_PLAYER_IDS, userName) != null;
        } catch (JedisConnectionException e) {
            logger.error("Redis connection exception occurred.", e);
            return false; // 假设连接异常时认为用户名不可用
        }
    }

    /**
     * 构建玩家
     *
     * @param loginChannel 网络连接通道
     * @param userName     用户昵称
     * @param password  密码
     * @return 玩家
     */
    private Player createPlayer(Channel loginChannel, String userName, String password) {
        int id = playerIdCreator.incrementAndGet();
        nextPlayerIdChanged = true;
        Player player = new Player(id, userName, password, loginChannel);
        if (!MyBatisUtils.insertPlayerInfo(player.getEntity().buildWeaponJson())) {
            return null;
        }
        JedisUtil.hset(USER_NAME_PLAYER_IDS, userName, String.valueOf(id));
        logger.info("Player created with id: {}, username: {}", id, userName);
        return player;
    }

    /**
     * 登录成功
     * @param player 玩家
     */
    private void onPlayerLoginSuccess(Player player) {
        PlayerService.getService().onPlayerLoginSuccess(player);
    }

    private void onPlayerRegisterSuccess(Player player) {
        PlayerService.getService().onPlayerLoginSuccess(player);
    }

    private Player loadPlayer(Channel senderChannel, String userName, String password) {
        PlayerInfoEntity entity = MyBatisUtils.getPlayerInfo(userName, password);
        if (entity != null) {
            return new Player(senderChannel, entity);
        }
        return null;
    }

    public void updateNextPlayerIdToDB() {
        if (!nextPlayerIdChanged) {
            return;
        }
        try (SqlSession session = MyBatisUtils.getSession()) {
            GameInfoMapper gameInfoMapper = session.getMapper(GameInfoMapper.class);
            gameInfoMapper.updateGameInfo(playerIdCreator.get());
            session.commit();
        } catch (Exception e) {
            logger.error("Failed to get game info: {}", e.getMessage());
        }
    }
}
