package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.MessageDispatcher;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.message.base.BasePlayerMessage;

public class ApplyBattleReqMessage extends BasePlayerMessage {

    @Expose
    private int weaponType;

    public ApplyBattleReqMessage() {
        setId(2001);
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
        player.onPlayerApplyBattle(weaponType);

        MessageDispatcher.getInstance().dispatchMessage(new SSEnterMatch(player.getRole()), null);
    }

    @Override
    public String description() {
        return "ApplyBattleReqMessage playerId: " + getPlayerId() + " weaponType:" + weaponType;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }
}
