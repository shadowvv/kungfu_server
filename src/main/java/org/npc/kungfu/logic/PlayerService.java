package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.ApplyBattleReqMessage;
import org.npc.kungfu.logic.message.BaseMessage;
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

    private BusStation<BaseMessage, Bus<BaseMessage>> taskStation;
    private ConcurrentHashMap<Integer, Player> idPlayers;

    public void init(BusStation<BaseMessage,Bus<BaseMessage>> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg) {
        if (msg instanceof ApplyBattleReqMessage) {
            ApplyBattleReqMessage req = (ApplyBattleReqMessage) msg;
            taskStation.put(req);
        }
    }

    public void onPlayerLoginOver(Player player) {
        idPlayers.putIfAbsent(player.getPlayerId(), player);
    }

    public Player getPlayer(int playerId) {
        return idPlayers.get(playerId);
    }

    public void onChannelInactive(Channel channel) {
    }
}
