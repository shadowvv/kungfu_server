package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.match.MatchRole;
import org.npc.kungfu.logic.message.base.BaseServerMatchMessage;

public class SSEnterMatch extends BaseServerMatchMessage {

    private MatchRole role;

    public SSEnterMatch(MatchRole role) {
        super(20003);
        this.role = role;
    }

    public MatchRole getRole() {
        return role;
    }

    public void setRole(MatchRole role) {
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
        player.sendApplyBattleSuccess(role.getRoleId(), role.getWeaponType());
        matchPool.enterMatch(role);
    }

    @Override
    public String description() {
        return "SSEnterMatch roleId:" + role.getRoleId();
    }
}
