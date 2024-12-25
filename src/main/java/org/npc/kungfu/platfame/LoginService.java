package org.npc.kungfu.platfame;

import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginReqMessage;

import java.util.concurrent.LinkedBlockingQueue;

public class LoginService {


    static LinkedBlockingQueue<LoginReqMessage> queue = new LinkedBlockingQueue<>();



    public LoginService(ITaskStation station) {

    }

    public static void onPlayerLogin(BaseMessage msg) {
        if (msg instanceof LoginReqMessage){
            queue.add((LoginReqMessage) msg);
        }
    }


}
