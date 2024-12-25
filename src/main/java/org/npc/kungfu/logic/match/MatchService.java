package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.BattleRingManager;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.platfame.ITaskStation;

import java.util.ArrayList;

public class MatchService {

    private static final ArrayList<Role> roles = new ArrayList<>();

    public MatchService(ITaskStation station) {

    }

    public static void enterMatch(Role role){
        roles.add(role);
    }

    public void matchUp(){
        BattleRingManager.startNewBattleRing(roles);
    }

}
