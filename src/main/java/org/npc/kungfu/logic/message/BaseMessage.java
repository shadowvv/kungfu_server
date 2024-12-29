package org.npc.kungfu.logic.message;

import org.npc.kungfu.net.LogicMessage;

public abstract class BaseMessage extends LogicMessage {

    public abstract MessageType getMessageType();

}
