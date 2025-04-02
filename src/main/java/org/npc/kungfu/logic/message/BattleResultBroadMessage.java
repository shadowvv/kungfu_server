package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

import java.util.List;

public class BattleResultBroadMessage extends BaseRespMessage {

    @Expose
    private List<RoleMessage> roles;
    @Expose
    private int winRoleId;

    public BattleResultBroadMessage() {
        super(MessageEnum.BATTLE_RESULT_BROAD.getId());
    }

    @Override
    public String description() {
        return "";
    }

    public List<RoleMessage> getRoleMessages() {
        return roles;
    }

    public void setRoleMessages(List<RoleMessage> roleMessages) {
        this.roles = roleMessages;
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
