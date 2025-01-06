package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

import java.util.List;

public class MatchResultBroadMessage extends BaseMessage {

    @Expose
    List<RoleMessage> roles;

    public MatchResultBroadMessage() {
        setId(10001);
    }

    @Override
    public void doLogic() {

    }

    @Override
    public String getDescription() {
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
