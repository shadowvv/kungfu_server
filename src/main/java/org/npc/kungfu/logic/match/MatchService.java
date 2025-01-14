package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;

public class MatchService {

    private static final MatchService service = new MatchService();

    private MatchService() {
    }

    public static MatchService getService() {
        return service;
    }

    private BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> taskStation;

    public void init(BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> matchStation) {
        this.taskStation = matchStation;
    }

    public void putMessage(BaseMessage msg) {
        taskStation.put(0L, msg);
    }
}
