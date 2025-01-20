package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.message.base.BaseServerPlayerMessage;


public class SSPlayerChannelReconnect extends BaseServerPlayerMessage {

    private final long playerId;
    private final Channel channel;

    public SSPlayerChannelReconnect(long playerId, Channel channel) {
        super(20002);
        this.playerId = playerId;
        this.channel = channel;
    }

    @Override
    public void doAction(Player player) {
        player.onPlayerReconnect(channel);
    }

    @Override
    public String description() {
        return "SSPlayerChannelReconnect player: " + playerId;
    }
}
