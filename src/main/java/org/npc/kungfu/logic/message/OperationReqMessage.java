package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.Role;

public class OperationReqMessage extends BaseMessage {

    @Expose
    private int roleId;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int faceAngle;

    public OperationReqMessage() {
        setId(3001);
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFaceAngle() {
        return faceAngle;
    }

    public void setFaceAngle(int faceAngle) {
        this.faceAngle = faceAngle;
    }

    public void doLogic(Role role) {
        if (role == null) {
            return;
        }
        role.onRoleMove(x, y);
        role.onRoleHit(faceAngle);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }
}
