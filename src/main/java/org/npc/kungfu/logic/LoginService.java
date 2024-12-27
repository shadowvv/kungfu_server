package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginReqMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;


public class LoginService {

    private static final LoginService service = new LoginService();

    public static LoginService getService() {
        return service;
    }

    public LoginService() {
    }

    private ITaskStation taskStation;

    public void init(ITaskStation station) {
        taskStation = station;
    }

    public void putMessage(BaseMessage msg, Channel senderChannel) {
        if (msg instanceof LoginReqMessage) {
            LoginReqMessage loginReqMsg = (LoginReqMessage) msg;
            loginReqMsg.setLoginChannel(senderChannel);
            taskStation.putMessage(msg);
        }
    }


    public void createPlayer(int playerId) {
    }

    public void LoadPlayer(int playerId) {

    }
}
