package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Role;

public class OperationReqMessage extends BaseMessage {

    private int x;
    private int y;
    private int faceAngle;

    public int getRoleId() {
        return 1;
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
        return null;
    }
}
