package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.MessageDispatcher;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.match.MatchRole;
import org.npc.kungfu.logic.message.base.BaseClientPlayerMessage;

public class ApplyBattleReqMessage extends BaseClientPlayerMessage {

    @Expose
    private int weaponType;

    public ApplyBattleReqMessage() {
        super(MessageEnum.APPLY_BATTLE_REQ.getId());
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int weaponType) {
        this.weaponType = weaponType;
    }

    @Override
    public void doAction(Player player) {
        if (player.isInBattle()) {
            getSenderChannel().writeAndFlush(new ErrorMessage(2001, ErrorCode.PLAYER_IN_BATTLE.getCode()));
            return;
        }
        if (player.isInMatch()) {
            getSenderChannel().writeAndFlush(new ErrorMessage(2001, ErrorCode.PLAYER_IN_MATCH.getCode()));
            return;
        }
        player.onPlayerApplyBattle(player.getPlayerId(), weaponType);
        MatchRole role = MatchRole.build(player.getPlayerId(), player.getPlayerId(), player.getUserName(), PlayerWeaponEnum.fromValue(weaponType));
        MessageDispatcher.getInstance().dispatchMessage(new SSEnterMatch(role), null);
    }

    @Override
    public String description() {
        return "ApplyBattleReqMessage playerId: " + getPlayerId() + " weaponType:" + weaponType;
    }

}
