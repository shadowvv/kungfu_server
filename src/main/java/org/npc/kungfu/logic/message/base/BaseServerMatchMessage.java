package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.message.MessageType;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseServerMatchMessage extends BaseServerMessage {

    public BaseServerMatchMessage(int id) {
        super(id);
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof MatchPool) {
            MatchPool pool = (MatchPool) passenger;
            doAction(pool);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }

    public abstract void doAction(MatchPool pool);

}
