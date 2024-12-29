package org.npc.kungfu.logic.message;

import java.util.List;

public class MatchResultBroadMessage extends BaseMessage {

    List<RoleMessage> roles;

    public MatchResultBroadMessage() {
        setId(10001);
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
