package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.database.MyBatisUtils;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.SimplePassenger;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;
import org.npc.kungfu.redis.JedisUtil;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录服务
 */
public class LoginService {

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
    /**
     * 使用玩家昵称作为登录的互斥量
     */
    private ConcurrentHashMap<String, Boolean> userNameMutex;
    /**
     * 使用网络连接作为登录的互斥量
     */
    private ConcurrentHashMap<Channel, Boolean> channelMutex;

    /**
     * 初始化
     * @param station 调度器
     */
    public void init(BusStation<SoloPassengerBus<SimplePassenger<BaseMessage>, BaseMessage>, SimplePassenger<BaseMessage>, BaseMessage> station) {
        taskStation = station;
        playerIdCreator = new AtomicInteger(0);
        channelMutex = new ConcurrentHashMap<>();
        userNameMutex = new ConcurrentHashMap<>();
    }

    /**
     * 投递消息
     * @param msg 消息
     */
    public void putMessage(BaseClientMessage msg) {
        taskStation.put(0, msg);
    }

    /**
     * 进入互斥区
     * @param userName 用户昵称
     * @return 是否成功
     */
    public Boolean enterUserNameMutex(String userName) {
        return userNameMutex.putIfAbsent(userName, true);
    }

    /**
     * 退出互斥区
     * @param userName 用户昵称
     */
    public void ExitMutex(String userName) {
        userNameMutex.remove(userName);
    }

    /**
     * 进入互斥区
     * @param senderChannel 网络通道
     * @return 是否成功
     */
    public Boolean enterChannelMutex(Channel senderChannel) {
        return channelMutex.putIfAbsent(senderChannel, true);
    }

    /**
     * 退出互斥区
     * @param senderChannel 网络通道
     */
    public void ExitMutex(Channel senderChannel) {
        channelMutex.remove(senderChannel);
    }

    /**
     * 检测用户昵称是否可用
     * @param userName 用户昵称
     * @return 是否可用
     */
    public boolean checkUserNameExist(String userName) {
        try {
            if (!JedisUtil.isConnectionOk()) {
                System.err.println("Redis connection is not available.");
                return false;
            }
            return JedisUtil.hget(USER_NAME_PLAYER_IDS, userName) != null;
        } catch (JedisConnectionException e) {
            // 记录日志或进行其他异常处理
            e.printStackTrace();
            return false; // 假设连接异常时认为用户名不可用
        }
    }

    /**
     * 构建玩家
     *
     * @param loginChannel 网络连接通道
     * @param userName     用户昵称
     * @param password
     * @return 玩家
     */
    public Player createPlayer(Channel loginChannel, String userName, String password) {
        int id = playerIdCreator.incrementAndGet();
        Player player = new Player(id, userName, password, loginChannel);
        JedisUtil.hset(USER_NAME_PLAYER_IDS, userName, String.valueOf(id));
        MyBatisUtils.insertPlayerInfo(player.getEntity());
        return player;
    }

    /**
     * 登录成功
     * @param player 玩家
     */
    //TODO:
    public void onPlayerLoginSuccess(Player player) {
        PlayerService.getService().onPlayerLoginSuccess(player);
    }

    /**
     * 加载玩家
     * @param playerId 玩家id
     * @param loginChannel 用户网络通道
     * @return 玩家
     */
    //TODO:
    public Player LoadPlayer(int playerId, Channel loginChannel) {
        return null;
    }

    /**
     * 玩家断开连接
     * @param channel 网络通道
     */
    //TODO:
    public void onChannelInactive(Channel channel) {
    }

    /**
     * 接收服务器消息
     * @param serverMessage 服务器消息
     */
    public void putMessage(BaseServerMessage serverMessage) {

    }

    public void onPlayerRegisterSuccess(Player player) {
    }

    public Player loadPlayer(Channel senderChannel, String userName, String password) {
        return null;
    }
}
