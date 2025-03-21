package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class RenameRespMessage extends BaseRespMessage {

    public RenameRespMessage() {
        super(MessageEnum.RENAME_RESP.getId());
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public String description() {
        return "";
    }
}
