package org.npc.kungfu.logic.message;

public class CancelMatchRespMessage extends BaseRespMessage {

    public CancelMatchRespMessage() {
        setId(3002);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }

    @Override
    public String description() {
        return "CancelMatchRespMessage playerId: " + getPlayerId();
    }
}
