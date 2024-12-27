package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.PlayerService;

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
        PlayerService.getService().onPlayerApplyBattle(this.playerId,this.weaponType);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }
}
