package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class BattleStateBroadMessage extends BaseRespMessage {

    @Expose
    private int battleState;

    public BattleStateBroadMessage(int battleState) {
        super(10004);
        this.battleState = battleState;
    }

    public int getBattleState() {
        return battleState;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }

    @Override
    public String description() {
        return "BattleStateBroadMessage";
    }
}
