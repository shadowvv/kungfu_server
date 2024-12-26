package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.message.OperationReqMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 战斗管理器
 */
public class BattleRingManager {

    /**
     * 战斗集合
     */
    private static HashMap<Integer, BattleRing> battleRingHashMap;

    public BattleRingManager() {

    }

    public static void startNewBattleRing(ArrayList<Role> roles) {
        BattleRing battleRing = BattleRing.build(roles);
        for (Role role : roles) {
            role.bindBattleId(battleRing.getBattleId());
        }
        battleRingHashMap.put(battleRing.getBattleId(), battleRing);
    }

    public void onReceiveMessage(int battleId, OperationReqMessage message) {
        BattleRing battleRing = battleRingHashMap.get(battleId);
        if (battleRing == null) {
            return;
        }
        battleRing.onReceiveMessage(message);
    }

}
