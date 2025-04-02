package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class CancelMatchRespMessage extends BaseRespMessage {

    public CancelMatchRespMessage() {
        super(MessageEnum.CANCEL_MATCH_RESP.getId());
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
