package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class BattleStartPushMessage extends BaseRespMessage {

    public BattleStartPushMessage() {
        super(10002);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }

    @Override
    public String description() {
        return "BattleStartPushMessage";
    }
}
