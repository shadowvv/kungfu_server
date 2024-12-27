package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleRingManager;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.SSStartMatchUpMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;
import org.npc.kungfu.platfame.bus.TaskStation;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class MatchService {

    private static final MatchService service = new MatchService();

    public static MatchService getService() {
        return service;
    }

    private MatchService() {}

    private final ConcurrentHashMap<Integer,Role> roles = new ConcurrentHashMap<>();
    private ITaskStation taskStation;

    public void init(TaskStation matchStation) {
        this.taskStation = matchStation;
    }

    public void enterMatch(Role role) {
        roles.put(role.getRoleId(), role);
        putMessage(new SSStartMatchUpMessage(),null);
    }

    public void matchUp() {
        //BattleRingManager.startNewBattleRing(roles);
    }

    public void putMessage(BaseMessage msg, Player player) {
        this.taskStation.putMessage(msg);
    }
}
