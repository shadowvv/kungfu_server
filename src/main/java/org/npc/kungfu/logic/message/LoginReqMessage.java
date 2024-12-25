package org.npc.kungfu.logic.message;

public class LoginReqMessage extends BaseMessage {

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
