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

    private BusStation<MatchPool> taskStation;

    public void init(BusStation<MatchPool> matchStation) {
        this.taskStation = matchStation;
        for (int i = 0; i < 5; i++) {
            MatchPool pool = new MatchPool();
            taskStation.put(pool);
        }
    }

    public void enterMatch(Role role) {
        MatchPool pool = new MatchPool();
        pool.enterPool(role);
    }

    public void putMessage(BaseMessage msg, int playerId) {
    }
}
