package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class LoginRespMessage extends BaseMessage {

    @Expose
    private int playerId;

    @Expose
    private boolean success;

    public LoginRespMessage() {
        setId(1002);
    }

    @Override
    public void doLogic() {

    }

    @Override
    public String getDescription() {
        return "";
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
