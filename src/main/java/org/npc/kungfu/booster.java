package org.npc.kungfu;

import org.npc.kungfu.net.WebSocketServer;
import org.npc.kungfu.platfame.MessageCoder;
import org.npc.kungfu.platfame.MessageDispatcher;

public class booster {

    public static void main(String[] args) throws InterruptedException {
        WebSocketServer net = new WebSocketServer(8080,new MessageDispatcher(),new MessageCoder<>());
        net.start();
    }

}
