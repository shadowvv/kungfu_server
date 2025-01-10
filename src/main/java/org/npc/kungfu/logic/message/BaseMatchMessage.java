package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseMatchMessage extends BaseMessage {

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof MatchPool) {
            MatchPool pool = (MatchPool) passenger;
            doAction(pool);
        }
    }

    public abstract void doAction(MatchPool pool);
}
