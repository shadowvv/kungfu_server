package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.message.base.BaseClientBattleMessage;

public class OperationReqMessage extends BaseClientBattleMessage {

    @Expose
    private int roleId;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int faceAngle;

    private Role role;

    public OperationReqMessage() {
        super(3001);
    }

    @Override
    public void doAction(BattleRing battleRing) {

        x = x / 1000;
        y = y / 1000;
        faceAngle = faceAngle / 1000;

        boolean operationSuccess = false;
        if (role.onRoleMove(x, y)) {
            if (role.onRoleHit(faceAngle)) {
                operationSuccess = true;
            }
        }
        OperationRespMessage msg = new OperationRespMessage();
        msg.setSuccess(operationSuccess);
        role.sendMessage(msg);
    }

    @Override
    public String description() {
        return "OperationReqMessage roleId: " + roleId + ", x: " + x + ", y: " + y + ", faceAngle: " + faceAngle;
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

    public void setRole(Role role) {
        this.role = role;
    }
}
