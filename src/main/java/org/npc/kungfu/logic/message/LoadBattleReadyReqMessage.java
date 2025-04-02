package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.message.base.BaseClientBattleMessage;

public class LoadBattleReadyReqMessage extends BaseClientBattleMessage {

    public LoadBattleReadyReqMessage() {
        super(MessageEnum.BATTLE_LOAD_READY_REQ.getId());
    }

    @Override
    public String description() {
        return "LoadBattleReadyReqMessage";
    }

    @Override
    public void doAction(BattleRing battleRing) {

    }
}
