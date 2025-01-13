package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseBattleMessage extends BaseMessage {

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {

    }

}
