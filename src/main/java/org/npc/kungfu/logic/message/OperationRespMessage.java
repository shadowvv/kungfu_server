package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class OperationRespMessage extends BaseMessage {

    @Expose
    private boolean success;

    public OperationRespMessage() {
        setId(3002);
    }

    @Override
    public void doLogic() {

    }

    @Override
    public String getDescription() {
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
