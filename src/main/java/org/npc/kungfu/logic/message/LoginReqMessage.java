package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.bus.ITask;

public class LoginReqMessage extends BaseClientMessage {

    @Expose
    private String userName;

    public LoginReqMessage() {
        super(1001);
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
    public void doAction(IPassenger<? extends ITask> passenger) {
        if (!getSenderChannel().isActive()) {
            return;
        }
        if (PlayerService.getService().getPlayerId(getSenderChannel()) != 0) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_CHANNEL_BIND_PLAYER.getCode()));
            return;
        }
        Boolean userNameMutex = LoginService.getService().enterUserNameMutex(userName);
        if (userNameMutex != null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            getSenderChannel().close();
            return;
        }
        Boolean channelMutex = LoginService.getService().enterChannelMutex(getSenderChannel());
        if (channelMutex != null) {
            LoginService.getService().ExitMutex(userName);
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            getSenderChannel().close();
            return;
        }

        if (!LoginService.getService().checkUserName(userName)) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            return;
        }

        Player player = PlayerService.getService().getPlayer(userName);
        if (player != null) {
            if (player.getChannel().isActive()) {
                //TODO:顶号
            } else {
                SSPlayerChannelReconnect ssPlayerChannelReconnect = new SSPlayerChannelReconnect(player.getId(), getSenderChannel());
                PlayerService.getService().putMessage(ssPlayerChannelReconnect);
            }
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            return;
        }

        player = LoginService.getService().createPlayer(getSenderChannel(), userName);
        if (player == null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.SYSTEM_ERROR.getCode()));
            LoginService.getService().enterUserNameMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            getSenderChannel().close();
            return;
        }

        LoginService.getService().onPlayerLoginSuccess(player);
        player.sendLoginSuccess();

        LoginService.getService().ExitMutex(userName);
        LoginService.getService().ExitMutex(getSenderChannel());
    }

    @Override
    public String description() {
        return "login request message userName:" + userName;
    }
}
