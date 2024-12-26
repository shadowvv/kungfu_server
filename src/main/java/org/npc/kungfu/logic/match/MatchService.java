package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.battle.BattleRingManager;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.platfame.TaskStation;

import java.util.ArrayList;

public class MatchService {

    private static final MatchService service = new MatchService();

    public static MatchService getService() {
        return service;
    }

    private static final ArrayList<Role> roles = new ArrayList<>();

    public MatchService() {

    }

    public void init(TaskStation matchStation) {
    }

    public static void enterMatch(Role role){
        roles.add(role);
    }

    public void matchUp(){
        BattleRingManager.startNewBattleRing(roles);
    }
}
