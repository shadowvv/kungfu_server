package org.npc.kungfu.logic;

import org.npc.kungfu.logic.message.ApplyBattleReqMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getService() {
        return service;
    }

    private BusStation<BaseMessage> taskStation;
    private ConcurrentHashMap<Integer, Player> idPlayers;

    public void init(BusStation<BaseMessage> playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg, int playerId) {
        if (msg instanceof ApplyBattleReqMessage) {
            ApplyBattleReqMessage req = (ApplyBattleReqMessage) msg;
            req.setPlayerId(playerId);
            taskStation.put(req);
        }
    }

    public void onPlayerLoginOver(Player player) {
        idPlayers.putIfAbsent(player.getPlayerId(), player);
    }

    public Player getPlayer(int playerId) {
        return idPlayers.get(playerId);
    }
}
