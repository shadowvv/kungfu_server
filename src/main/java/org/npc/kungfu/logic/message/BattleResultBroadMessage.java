package org.npc.kungfu.logic.message;

import java.util.List;

public class BattleResultBroadMessage extends BaseRespMessage {

    private List<RoleMessage> roleMessages;
    private int winRoleId;

    public BattleResultBroadMessage() {

    }

    @Override
    public String description() {
        return "";
    }

    public List<RoleMessage> getRoleMessages() {
        return roleMessages;
    }

    public void setRoleMessages(List<RoleMessage> roleMessages) {
        this.roleMessages = roleMessages;
    }

    public int getWinRoleId() {
        return winRoleId;
    }

    public void setWinRoleId(int winRoleId) {
        this.winRoleId = winRoleId;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
