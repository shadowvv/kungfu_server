package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
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
        // 如果channel已经关闭，则不处理
        if (!getSenderChannel().isActive()) {
            return;
        }
        // 如果channel已经登录，则不处理
        if (PlayerService.getService().getPlayerId(getSenderChannel()) != 0) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_CHANNEL_BIND_PLAYER.getCode()));
            return;
        }
        // 如果用户名正在登录，则不处理
        Boolean userNameMutex = LoginService.getService().enterUserNameMutex(userName);
        if (userNameMutex != null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            getSenderChannel().close();
            return;
        }
        // 如果channel正在登录，则不处理
        Boolean channelMutex = LoginService.getService().enterChannelMutex(getSenderChannel());
        if (channelMutex != null) {
            LoginService.getService().ExitMutex(userName);
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_IS_LOGGING.getCode()));
            getSenderChannel().close();
            return;
        }
        // 如果用户名存在，则不处理
        if (LoginService.getService().checkUserNameExist(userName)) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            return;
        }
        // 如果用户名在线
        Player player = PlayerService.getService().getPlayer(userName);
        if (player != null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.LOGIN_SAME_USERNAME.getCode()));
            LoginService.getService().ExitMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            return;
        }

        // 创建玩家
        player = LoginService.getService().createPlayer(getSenderChannel(), userName, password);
        if (player == null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(getId(), ErrorCode.SYSTEM_ERROR.getCode()));
            LoginService.getService().enterUserNameMutex(userName);
            LoginService.getService().ExitMutex(getSenderChannel());
            getSenderChannel().close();
            return;
        }
        // 登录成功
        LoginService.getService().onPlayerRegisterSuccess(player);
        player.sendRegisterSuccess();

        // 退出互斥
        LoginService.getService().ExitMutex(userName);
        LoginService.getService().ExitMutex(getSenderChannel());
    }

    @Override
    public String description() {
        return "";
    }
}
