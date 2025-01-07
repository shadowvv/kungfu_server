package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class LoginRespMessage extends BaseRespMessage {

    @Expose
    private int playerId;

    @Expose
    private boolean success;

    public LoginRespMessage() {
        setId(1002);
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
    public String getDescription() {
        return "login successful playerId: " + playerId;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
