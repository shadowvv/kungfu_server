package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.OperationReqMessage;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.HashMap;
import java.util.List;

public class BattleService {

    private static final BattleService service = new BattleService();

    public static BattleService getService() {
        return service;
    }

    private BattleService() {

    }

    private BusStation<BattleRing> taskStation;
    /**
     * 战斗集合
     */
    private static HashMap<Integer, BattleRing> battleRingHashMap;

    public void init(BusStation<BattleRing> battleStation) {
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
            role.bindBattleId(battleRing.getId());
        }
        battleRingHashMap.put(battleRing.getId(), battleRing);
        taskStation.put(battleRing);
    }
}
