package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseRespMessage extends BaseClientMessage {

    public BaseRespMessage(int id) {
        super(id);
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {

    }
}
