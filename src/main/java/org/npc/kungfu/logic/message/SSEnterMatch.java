package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public class SSEnterMatch extends BaseMatchMessage {

    private Role role;

    public SSEnterMatch(Role role) {
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
        Player player = PlayerService.getService().getPlayer(getPlayerId());
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
        MatchService.getService().enterMatch(role);
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (passenger instanceof MatchPool) {
            MatchPool pool = (MatchPool) passenger;

        }
    }

    @Override
    public String description() {
        return "SSEnterMatch roleId:" + role.getRoleId();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }
}
