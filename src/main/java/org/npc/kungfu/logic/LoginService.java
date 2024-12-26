package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginReqMessage;
import org.npc.kungfu.platfame.ITaskStation;
import org.npc.kungfu.platfame.TaskBus;


public class LoginService {

    private static final LoginService service = new LoginService();

    public static LoginService getService() {
        return service;
    }

    private final TaskBus taskBus = new TaskBus();

    public LoginService() {
    }

    public void init(ITaskStation station){
        station.addBus(taskBus);
    }

    public void onPlayerLogin(BaseMessage msg, Channel senderChannel) {
        if (msg instanceof LoginReqMessage){
            LoginReqMessage loginReqMsg = (LoginReqMessage) msg;
            loginReqMsg.setLoginChannel(senderChannel);
            taskBus.addMessage(msg);
        }
    }


}
