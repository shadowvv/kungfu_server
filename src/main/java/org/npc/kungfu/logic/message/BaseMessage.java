package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.net.LogicMessage;

public abstract class BaseMessage extends LogicMessage {

    private Channel senderChannel;

    public abstract MessageType getMessageType();

    public void setSenderChannel(Channel senderChannel) {
        this.senderChannel = senderChannel;
    }

    public Channel getSenderChannel() {
        return senderChannel;
    }
}
