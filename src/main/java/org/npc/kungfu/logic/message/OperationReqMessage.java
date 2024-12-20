package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Role;

public class OperationReqMessage {

    private int x;
    private int y;
    private int faceAngle;

    public int getRoleId() {
        return 1;
    }

    public void doLogic(Role role) {
        if (role == null){
            return;
        }
        role.onRoleMove(x,y);
        role.onRoleHit(faceAngle);
    }
}
