package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.OperationReqMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;
import org.npc.kungfu.platfame.bus.TaskStation;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class BattleService {

    private static final BattleService service = new BattleService();

    public static BattleService getService() {
        return service;
    }

    private BattleService() {

    }

    private ITaskStation taskStation;
    /**
     * 战斗集合
     */
    private static HashMap<Integer, BattleRing> battleRingHashMap;

    public void init(TaskStation battleStation) {
        taskStation = battleStation;
    }

    public void putMessage(BaseMessage msg, int playerId) {
        Player player = PlayerService.getService().getPlayer(playerId);
        if (player == null) {
            return;
        }
        int battleId = player.getRole().getBattleId();
        BattleRing ring = battleRingHashMap.get(battleId);
        if (ring == null) {
            return;
        }
        ring.onReceiveMessage((OperationReqMessage) msg);
    }

    public void startBattle(List<Role> roles) {
        BattleRing battleRing = BattleRing.build(roles);
        for (Role role : roles) {
            role.bindBattleId(battleRing.getBattleId());
        }
        battleRingHashMap.put(battleRing.getBattleId(), battleRing);
        taskStation.putRunnable(battleRing);
    }
}
