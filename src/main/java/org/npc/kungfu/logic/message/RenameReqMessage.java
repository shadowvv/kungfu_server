package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public class RenameReqMessage extends BaseClientMessage {

    @Expose
    private String nickname;

    public RenameReqMessage() {
        super(MessageEnum.RENAME_REQ.getId());
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {

    }

    @Override
    public String description() {
        return "";
    }
}
