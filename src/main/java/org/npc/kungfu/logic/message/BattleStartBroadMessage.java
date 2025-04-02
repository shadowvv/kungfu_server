package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

import java.util.List;

public class BattleStartBroadMessage extends BaseRespMessage {

    @Expose
    List<RoleMessage> roles;

    public BattleStartBroadMessage() {
        super(MessageEnum.BATTLE_START_BROAD_MESSAGE.getId());
    }

    public List<RoleMessage> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleMessage> roles) {
        this.roles = roles;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }

    @Override
    public String description() {
        return "BattleStartPushMessage";
    }
}
