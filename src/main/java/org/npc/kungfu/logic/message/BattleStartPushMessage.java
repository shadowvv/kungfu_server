package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class BattleStartPushMessage extends BaseRespMessage {

    @Expose
    private int gameState;

    public BattleStartPushMessage(int gameState) {
        super(10002);
        this.gameState = gameState;
    }

    public int getGameState() {
        return gameState;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.BATTLE_MESSAGE;
    }

    @Override
    public String description() {
        return "BattleStartPushMessage";
    }
}
