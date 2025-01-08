package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.SSPlayerChannelInactive;
import org.npc.kungfu.platfame.bus.FixedBusStation;
import org.npc.kungfu.platfame.bus.FixedPassengerBus;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }
    public static PlayerService getService() {
        return service;
    }

    private FixedBusStation<Player, FixedPassengerBus<Player, BaseMessage>, BaseMessage> taskStation;
    private ConcurrentHashMap<Integer, Player> idPlayers;
    private ConcurrentHashMap<Channel, Integer> channelPlayerIds;
    private ConcurrentHashMap<String, Player> usernamePlayers;

    public void init(FixedBusStation<Player, FixedPassengerBus<Player, BaseMessage>, BaseMessage> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        channelPlayerIds = new ConcurrentHashMap<>();
        usernamePlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg) {
        Player player = idPlayers.get(msg.getPlayerId());
        if (player != null) {
            taskStation.putLuggage(player, msg);
        }
    }

    public void onPlayerLoginSuccess(Player player) {
        idPlayers.putIfAbsent(player.getPlayerId(), player);
        channelPlayerIds.putIfAbsent(player.getChannel(), player.getPlayerId());
        usernamePlayers.putIfAbsent(player.getUserName(), player);
        taskStation.put(player);
    }

    public Player getPlayer(int playerId) {
        return idPlayers.get(playerId);
    }

    public Player getPlayer(String username) {
        return usernamePlayers.get(username);
    }

    public int getPlayerId(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return 0;
        }
        return channelPlayerIds.get(channel);
    }

    public void onChannelInactive(Channel channel) {
        if (!channelPlayerIds.containsKey(channel)) {
            return;
        }
        int playerId = channelPlayerIds.get(channel);
        Player player = idPlayers.get(playerId);
        if (player != null) {
            SSPlayerChannelInactive ssPlayerChannelInactive = new SSPlayerChannelInactive();
            ssPlayerChannelInactive.setPlayerId(playerId);
            putMessage(ssPlayerChannelInactive);
        }
    }

    public void removePlayer(Player player) {
        idPlayers.remove(player.getPlayerId());
        channelPlayerIds.remove(player.getChannel());
        usernamePlayers.remove(player.getUserName());
        taskStation.remove(player);
    }
}
