package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchService;

public class ApplyBattleReqMessage extends BaseMessage {

    @Expose
    private int weaponType;

    private int playerId;

    public ApplyBattleReqMessage() {
        setId(2001);
    }

    public int getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(int weaponType) {
        this.weaponType = weaponType;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    @Override
    public void doLogic() {
        Player player = PlayerService.getService().getPlayer(playerId);
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
        MatchService.getService().enterMatch(player.getRole());
        player.sendApplyBattleSuccess();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }
}
