package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class RoleMessage {

    @Expose
    private int roleId;
    @Expose
    private String userName;
    @Expose
    private int weaponType;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int faceAngle;

    public RoleMessage() {

    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int weaponType) {
        this.weaponType = weaponType;
    }

    public int getX() {
        return x / 1000;
    }

    public void setX(int x) {
        this.x = x * 1000;
    }

    public int getY() {
        return y / 1000;
    }

    public void setY(int y) {
        this.y = y * 1000;
    }

    public int getFaceAngle() {
        return faceAngle / 1000;
    }

    public void setFaceAngle(int faceAngle) {
        this.faceAngle = faceAngle * 1000;
    }
}
