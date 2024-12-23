package org.npc.kungfu.platfame;

import org.npc.kungfu.logic.message.LogicMessage;
import org.npc.kungfu.net.IMessageDispatcher;

public class MessageDispatcher implements IMessageDispatcher {

    @Override
    public void dispatchMessage(Object message) {
        if (message instanceof LogicMessage) {

        }
    }
}
