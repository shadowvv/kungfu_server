package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class LoginRespMessage extends BaseRespMessage {

    @Expose
    private PlayerInfoMessage playerInfo;

    public LoginRespMessage() {
        super(1002);
    }

    public PlayerInfoMessage getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfoMessage playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public String description() {
        return "login successful playerId: " + playerInfo.getPlayerId();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
