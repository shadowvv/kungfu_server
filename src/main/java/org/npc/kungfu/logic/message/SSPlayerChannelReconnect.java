package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.Player;


public class SSPlayerChannelReconnect extends BasePlayerMessage {

    private Channel channel;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public void doAction(Player player) {
        player.onPlayerReconnect(channel);
    }

    @Override
    public String description() {
        return "SSPlayerChannelReconnect player: " + getPlayerId();
    }
}
