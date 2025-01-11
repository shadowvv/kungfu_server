package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BasePlayerMessage extends BaseMessage {

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof Player) {
            Player player = (Player) passenger;
            doAction(player);
        }
    }

    public abstract void doAction(Player player);
}
