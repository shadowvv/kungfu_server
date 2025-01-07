package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
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

    public void init(FixedBusStation<Player, FixedPassengerBus<Player, BaseMessage>, BaseMessage> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
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
        taskStation.put(player);
    }

    public Player getPlayer(int playerId) {
        return idPlayers.get(playerId);
    }

    public void onChannelInactive(Channel channel) {
    }
}
