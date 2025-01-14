package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.message.MessageType;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseServerPlayerMessage extends BaseServerMessage {
    /**
     * @param id 消息id
     */
    public BaseServerPlayerMessage(int id) {
        super(id);
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof Player) {
            Player player = (Player) passenger;
            doAction(player);
        }
    }

    public abstract void doAction(Player player);

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }
}
