package org.npc.kungfu.logic.message;

import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseRespMessage extends BaseMessage {

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {

    }
}
