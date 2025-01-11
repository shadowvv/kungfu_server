package org.npc.kungfu.logic.message.base;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.MessageType;
import org.npc.kungfu.net.LogicMessage;

public abstract class BaseMessage extends LogicMessage {

    private Channel senderChannel;
    private long playerId;

    public void setSenderChannel(Channel senderChannel) {
        this.senderChannel = senderChannel;
    }

    public Channel getSenderChannel() {
        return senderChannel;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    public abstract MessageType getMessageType();
}
