package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;

public class LoginReqMessage extends BaseMessage {

    @Expose
    private String userName;

    public LoginReqMessage() {
        setId(1001);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.LOGIN_MESSAGE;
    }

    @Override
    public void doLogic() {
        Boolean mutex = LoginService.getService().enterMutex(userName);
        if (mutex != null) {
            return;
        }
        Player player = LoginService.getService().createPlayer(getSenderChannel(), userName);;
        if (player != null) {
            PlayerService.getService().onPlayerLoginOver(player);
            player.sendLoginSuccess();
        }
        LoginService.getService().ExitMutex(userName);
    }

    @Override
    public String getDescription() {
        return "";
    }
}
