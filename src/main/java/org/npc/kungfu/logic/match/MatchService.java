package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.BusStation;

public class MatchService {

    private static final MatchService service = new MatchService();

    private MatchService() {
    }

    public static MatchService getService() {
        return service;
    }

    private MatchBusStation taskStation;

    public void init(MatchBusStation matchStation) {
        this.taskStation = matchStation;
    }

    public void enterMatch(Role role) {
        this.taskStation.put(role);
    }
}
