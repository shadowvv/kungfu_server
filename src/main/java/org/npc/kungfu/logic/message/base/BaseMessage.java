package org.npc.kungfu.logic.message.base;

import org.npc.kungfu.logic.message.MessageType;
import org.npc.kungfu.net.LogicMessage;

public abstract class BaseMessage extends LogicMessage {
    /**
     * @param id 消息id
     */
    public BaseMessage(int id) {
        super(id);
    }


    public abstract MessageType getMessageType();
}
