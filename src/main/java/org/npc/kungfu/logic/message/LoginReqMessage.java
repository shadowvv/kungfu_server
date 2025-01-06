package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.Player;

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
        if (!getSenderChannel().isActive()) {
            return;
        }
        Boolean mutex = LoginService.getService().enterMutex(userName);
        if (mutex != null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            getSenderChannel().close();
            return;
        }
        if (!LoginService.getService().checkUserName(userName)) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().enterMutex(userName);
            getSenderChannel().close();
            return;
        }
        Player player = LoginService.getService().createPlayer(getSenderChannel(), userName);
        if (player == null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.SYSTEM_ERROR.getCode()));
            LoginService.getService().enterMutex(userName);
            getSenderChannel().close();
            return;
        }
        LoginService.getService().onPlayerLoginSuccess(player);
        player.sendLoginSuccess();

        LoginService.getService().ExitMutex(userName);
    }

    @Override
    public String getDescription() {
        return "login request message userName:" + userName;
    }
}
