package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.SSPlayerChannelInactive;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 玩家管理服务
 */
public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getService() {
        return service;
    }

    /**
     * 玩家业务调度器
     */
    private BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> taskStation;
    /**
     * 玩家id映射
     */
    private ConcurrentHashMap<Long, Player> idPlayers;
    /**
     * 玩家通道映射
     */
    private ConcurrentHashMap<Channel, Long> channelPlayerIds;
    /**
     * 玩家用户名映射s
     */
    private ConcurrentHashMap<String, Player> usernamePlayers;

    /**
     * 初始化服务
     *
     * @param playerStation 玩家业务调度器
     */
    public void init(BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        channelPlayerIds = new ConcurrentHashMap<>();
        usernamePlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    /**
     * 投递消息
     *
     * @param msg 消息
     */
    public void putMessage(BaseClientMessage msg) {
        taskStation.put(msg.getPlayerId(), msg);
    }

    public void putMessage(BaseServerMessage msg) {
        if (msg instanceof SSPlayerChannelInactive) {
            SSPlayerChannelInactive ss = (SSPlayerChannelInactive) msg;
            taskStation.put(ss.getPlayerId(), ss);
        }
    }

    /**
     * 玩家登录成功
     *
     * @param player 玩家
     */
    public void onPlayerLoginSuccess(Player player) {
        idPlayers.putIfAbsent(player.getId(), player);
        channelPlayerIds.putIfAbsent(player.getChannel(), player.getId());
        usernamePlayers.putIfAbsent(player.getUserName(), player);
        taskStation.put(player);
    }

    /**
     * 玩家断开连接
     *
     * @param channel 通道
     */
    public void onChannelInactive(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return;
        }
        long playerId = channelPlayerIds.get(channel);
        Player player = idPlayers.get(playerId);
        if (player != null) {
            SSPlayerChannelInactive ssPlayerChannelInactive = new SSPlayerChannelInactive(playerId);
            putMessage(ssPlayerChannelInactive);
        }
    }

    /**
     * 移除玩家
     *
     * @param player 要移除的玩家
     * @return 是否成功
     */
    public boolean removePlayer(Player player) {
        idPlayers.remove(player.getId());
        channelPlayerIds.remove(player.getChannel());
        usernamePlayers.remove(player.getUserName());
        return taskStation.remove(player);
    }

    /**
     * @param playerId 玩家id
     * @return 玩家
     */
    public Player getPlayer(long playerId) {
        return idPlayers.get(playerId);
    }

    /**
     * @param username 用户名
     * @return 玩家
     */
    public Player getPlayer(String username) {
        return usernamePlayers.get(username);
    }

    /**
     * @param channel
     * @return
     */
    public Player getPlayer(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return null;
        }
        long playerId = channelPlayerIds.get(channel);
        return idPlayers.get(playerId);
    }

    /**
     * @param channel 通道
     * @return 玩家
     */
    public long getPlayerId(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return 0;
        }
        return channelPlayerIds.get(channel);
    }
}
