package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleService;

import java.util.List;

public class SSCreateBattleMessage extends BaseMessage {

    private List<Role> roles;

    public SSCreateBattleMessage(List<Role> list) {
        setId(20002);
        roles = list;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }

    @Override
    public void doLogic() {

    }
}
