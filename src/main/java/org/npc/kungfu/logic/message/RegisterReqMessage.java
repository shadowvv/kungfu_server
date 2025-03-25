package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public class RegisterReqMessage extends BaseClientMessage {

    @Expose
    private String userName;
    @Expose
    private String password;

    public RegisterReqMessage() {
        super(MessageEnum.REGISTER_REQ.getId());
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }

    @Override
    public void doAction(IPassenger<? extends ITask> passenger) {
        LoginService.getService().onPlayerRegister(getSenderChannel(), userName, password);
    }

    @Override
    public String description() {
        return "";
    }
}
