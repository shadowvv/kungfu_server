package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class ApplyBattleRespMessage extends BaseRespMessage {

    @Expose
    private int roleId;
    @Expose
    private int weaponType;

    public ApplyBattleRespMessage() {
        super(2002);
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int weaponType) {
        this.weaponType = weaponType;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public String description() {
        return "ApplyBattleRespMessage playerId:" + getPlayerId() + " roleId:" + roleId + " weaponType:" + weaponType;
    }
}
