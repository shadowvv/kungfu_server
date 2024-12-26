package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.PlayerService;

public class LoginReqMessage extends BaseMessage {

    public int playerId;

    private Channel loginChannel;

    public LoginReqMessage() {
        setId(1001);
    }

    public void setLoginChannel(Channel loginChannel) {
        this.loginChannel = loginChannel;
    }

    @Override
    public void doLogic() {
        if (playerId == 0) {
            PlayerService.getService().newPlayerLoginOver(loginChannel);
        } else {
            PlayerService.getService().onPlayerLoginOver(playerId, loginChannel);
        }
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }
}
