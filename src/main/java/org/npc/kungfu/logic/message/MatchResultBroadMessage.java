package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

import java.util.List;

public class MatchResultBroadMessage extends BaseRespMessage {

    @Expose
    List<RoleMessage> roles;

    public MatchResultBroadMessage() {
        setId(10001);
    }

    @Override
    public String description() {
        return "";
    }

    public List<RoleMessage> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleMessage> roles) {
        this.roles = roles;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }
}
