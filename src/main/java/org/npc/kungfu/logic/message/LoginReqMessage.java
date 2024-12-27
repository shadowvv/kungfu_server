package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import io.netty.channel.Channel;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.PlayerService;

public class LoginReqMessage extends BaseMessage {

    @Expose
    private int playerId;

    private Channel loginChannel;

    public LoginReqMessage() {
        setId(1001);
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void setLoginChannel(Channel loginChannel) {
        this.loginChannel = loginChannel;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }

    @Override
    public void doLogic() {
        if (playerId == 0) {
            LoginService.getService().createPlayer(playerId);
            PlayerService.getService().newPlayerLoginOver(loginChannel);
        } else {
            LoginService.getService().LoadPlayer(playerId);
            PlayerService.getService().onPlayerLoginOver(playerId, loginChannel);
        }
    }
}
