package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.battle.BattleRole;
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

    private BattleRole role;

    public OperationReqMessage() {
        super(MessageEnum.OPERATION_REQ.getId());
    }

    @Override
    public void doAction(BattleRing battleRing) {

        double tempX = x / (1000 * 1000);
        double tempY = y / (1000 * 1000);
        double tempAngle = faceAngle / (1000 * 1000);

        boolean operationSuccess = false;
        if (role.onRoleMove(tempX, tempY)) {
            if (role.onRoleHit(tempAngle)) {
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

    public void setRole(BattleRole role) {
        this.role = role;
    }
}
