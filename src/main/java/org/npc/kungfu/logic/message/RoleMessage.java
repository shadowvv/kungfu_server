package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class RoleMessage {

    @Expose
    private int roleId;

    @Expose
    private int weaponType;

    public RoleMessage() {

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
}
