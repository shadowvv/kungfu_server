package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.net.LogicMessage;

public abstract class BaseMessage extends LogicMessage {

    private Channel senderChannel;
    private int playerId;

    public void setSenderChannel(Channel senderChannel) {
        this.senderChannel = senderChannel;
    }

    public Channel getSenderChannel() {
        return senderChannel;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    public abstract MessageType getMessageType();
}
