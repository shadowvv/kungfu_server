package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.net.LogicMessage;

public class MessageBus extends Bus<LogicMessage> {

    public MessageBus(String signature) {
        super(signature);
    }
}
