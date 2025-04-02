package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class LoadBattleReadyRespMessage extends BaseRespMessage {

    public LoadBattleReadyRespMessage() {
        super(MessageEnum.BATTLE_LOAD_READY_RESP.getId());
    }

    @Override
    public MessageType getMessageType() {
        return null;
    }

    @Override
    public String description() {
        return "";
    }
}
