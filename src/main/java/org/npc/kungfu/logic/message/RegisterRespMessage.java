package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class RegisterRespMessage extends BaseRespMessage {

    @Expose
    private PlayerInfoMessage playerInfo;

    public RegisterRespMessage() {
        super(MessageEnum.REGISTER_RESP.getId());
    }

    public PlayerInfoMessage getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfoMessage playerInfo) {
        this.playerInfo = playerInfo;
    }

    @Override
    public MessageType getMessageType() {
        return null;
    }

    @Override
    public String description() {
        return "";
    }
}
