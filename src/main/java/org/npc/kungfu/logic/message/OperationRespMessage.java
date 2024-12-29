package org.npc.kungfu.logic.message;

public class OperationRespMessage extends BaseMessage {

    public OperationRespMessage() {
        setId(3002);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
