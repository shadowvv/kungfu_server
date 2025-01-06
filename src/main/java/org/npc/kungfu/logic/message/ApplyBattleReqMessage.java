package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchService;

public class ApplyBattleReqMessage extends BaseMessage {

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
    public void doLogic() {
        Player player = PlayerService.getService().getPlayer(getPlayerId());
        if (player == null) {
            return;
        }
        if (player.isInBattle()) {
            return;
        }
        if (player.isInMatch()) {
            return;
        }
        player.onPlayerApplyBattle(weaponType);
        player.sendApplyBattleSuccess();

        MatchService.getService().enterMatch(player.getRole());
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }
}
