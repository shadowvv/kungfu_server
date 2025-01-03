package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

import java.util.List;

public class OperationRespMessage extends BaseMessage {

    @Expose
    List<RoleMessage> roleMessages;

    public OperationRespMessage() {
        setId(3002);
    }

    public List<RoleMessage> getRoleMessages() {
        return roleMessages;
    }

    public void setRoleMessages(List<RoleMessage> roleMessages) {
        this.roleMessages = roleMessages;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
