package org.npc.kungfu.logic.message.base;

import io.netty.channel.Channel;

public abstract class BaseClientMessage extends BaseMessage {

    private Channel senderChannel;
    private long playerId;

    public BaseClientMessage(int id) {
        super(id);
    }

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
}
