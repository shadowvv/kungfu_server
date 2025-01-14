package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class LoginRespMessage extends BaseRespMessage {

    @Expose
    private long playerId;

    @Expose
    private boolean success;

    public LoginRespMessage() {
        super(1002);
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String description() {
        return "login successful playerId: " + playerId;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
