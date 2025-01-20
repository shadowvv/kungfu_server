package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.message.base.BaseClientBattleMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public class OperationReqMessage extends BaseClientBattleMessage {

    @Expose
    private int roleId;
    @Expose
    private int x;
    @Expose
    private int y;
    @Expose
    private int faceAngle;

    public OperationReqMessage() {
        super(3001);
    }


    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {

    }

    @Override
    public void doAction(BattleRing battleRing) {

    }

    @Override
    public String description() {
        return "";
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

}
