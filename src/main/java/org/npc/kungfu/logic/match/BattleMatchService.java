package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.BattleRingManager;
import org.npc.kungfu.logic.Role;

import java.util.ArrayList;

public class BattleMatchService {

    private static final ArrayList<Role> roles = new ArrayList<>();

    public BattleMatchService() {

    }

    public static void enterMatch(Role role){
        roles.add(role);
    }

    public void matchUp(){
        BattleRingManager.startNewBattleRing(roles);
    }

}
