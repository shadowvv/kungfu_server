package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class OperationRespMessage extends BaseRespMessage {

    @Expose
    private boolean success;

    public OperationRespMessage() {
        super(3002);
    }

    @Override
    public String description() {
        return "";
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
