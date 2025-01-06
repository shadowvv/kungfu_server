package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class ApplyBattleRespMessage extends BaseMessage {

    @Expose
    private int roleId;
    @Expose
    private int weaponType;

    public ApplyBattleRespMessage() {
        setId(2002);
    }

    @Override
    public void doLogic() {

    }

    @Override
    public String getDescription() {
        return "";
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
}
