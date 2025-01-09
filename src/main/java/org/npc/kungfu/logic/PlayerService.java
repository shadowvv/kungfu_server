package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.SSPlayerChannelInactive;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }
    public static PlayerService getService() {
        return service;
    }

    private BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> taskStation;
    private ConcurrentHashMap<Long, Player> idPlayers;
    private ConcurrentHashMap<Channel, Long> channelPlayerIds;
    private ConcurrentHashMap<String, Player> usernamePlayers;

    public void init(BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        channelPlayerIds = new ConcurrentHashMap<>();
        usernamePlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg) {
        taskStation.put(msg.getPlayerId(), msg);
    }

    public void onPlayerLoginSuccess(Player player) {
        idPlayers.putIfAbsent(player.getId(), player);
        channelPlayerIds.putIfAbsent(player.getChannel(), player.getId());
        usernamePlayers.putIfAbsent(player.getUserName(), player);
        taskStation.put(player);
    }

    public Player getPlayer(long playerId) {
        return idPlayers.get(playerId);
    }

    public Player getPlayer(String username) {
        return usernamePlayers.get(username);
    }

    public long getPlayerId(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return 0;
        }
        return channelPlayerIds.get(channel);
    }

    public void onChannelInactive(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return;
        }
        long playerId = channelPlayerIds.get(channel);
        Player player = idPlayers.get(playerId);
        if (player != null) {
            SSPlayerChannelInactive ssPlayerChannelInactive = new SSPlayerChannelInactive();
            ssPlayerChannelInactive.setPlayerId(playerId);
            putMessage(ssPlayerChannelInactive);
        }
    }

    public void removePlayer(Player player) {
        idPlayers.remove(player.getId());
        channelPlayerIds.remove(player.getChannel());
        usernamePlayers.remove(player.getUserName());
        taskStation.remove(player);
    }
}
