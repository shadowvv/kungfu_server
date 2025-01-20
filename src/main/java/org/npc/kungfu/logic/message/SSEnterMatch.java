package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.message.base.BaseServerMatchMessage;

public class SSEnterMatch extends BaseServerMatchMessage {

    private Role role;

    public SSEnterMatch(Role role) {
        super(20003);
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public void doAction(MatchPool matchPool) {
        Player player = PlayerService.getService().getPlayer(role.getPlayerId());
        if (player == null) {
            return;
        }
        if (player.isInBattle()) {
            player.sendMessage(new ErrorMessage(2001, ErrorCode.PLAYER_IN_BATTLE.getCode()));
            return;
        }
        if (!player.isInMatch()) {
            player.sendMessage(new ErrorMessage(2001, ErrorCode.PLAYER_IN_MATCH.getCode()));
            return;
        }
        player.sendApplyBattleSuccess();
        matchPool.enterMatch(role);
    }

    @Override
    public String description() {
        return "SSEnterMatch roleId:" + role.getRoleId();
    }
}
