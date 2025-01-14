package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.message.MessageType;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class BaseClientBattleMessage extends BaseClientMessage {

    public BaseClientBattleMessage(int id) {
        super(id);
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof BattleRing) {
            BattleRing battleRing = (BattleRing) passenger;
            doAction(battleRing);
        }
    }

    public abstract void doAction(BattleRing battleRing);

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
