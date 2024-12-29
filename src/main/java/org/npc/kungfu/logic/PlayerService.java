package org.npc.kungfu.logic;

import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.ApplyBattleReqMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;
import org.npc.kungfu.platfame.bus.TaskStation;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getService() {
        return service;
    }

    private ConcurrentHashMap<Integer, Player> idPlayers;
    private ITaskStation taskStation;

    public void init(TaskStation playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg, int playerId) {
        if (msg instanceof ApplyBattleReqMessage) {
            ApplyBattleReqMessage req = (ApplyBattleReqMessage) msg;
            req.setPlayerId(playerId);
            taskStation.putMessage(req);
        }
    }

    public void onPlayerLoginOver(Player player) {
        if (idPlayers.containsKey(player.getPlayerId())) {
            return;
        }
        idPlayers.put(player.getPlayerId(), player);
    }

    public Player getPlayer(int playerId) {
        return idPlayers.get(playerId);
    }
}
