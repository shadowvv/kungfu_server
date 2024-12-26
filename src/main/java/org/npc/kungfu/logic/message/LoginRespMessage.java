package org.npc.kungfu.logic.message;

public class LoginRespMessage  extends BaseMessage {

    public boolean success;

    public LoginRespMessage(boolean success) {
        this.success = success;
    }

    @Override
    public MessageType getMessageType() {
        return null;
    }
}
