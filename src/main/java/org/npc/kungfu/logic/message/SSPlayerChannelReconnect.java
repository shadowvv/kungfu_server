package org.npc.kungfu.logic.message;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;


public class SSPlayerChannelReconnect extends BaseMessage {

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
    public void doLogic() {
        Player player = PlayerService.getService().getPlayer(getPlayerId());
        if (player != null) {
            player.onPlayerReconnect(channel);
        }
    }

    @Override
    public String getDescription() {
        return "SSPlayerChannelReconnect player: " + getPlayerId();
    }
}
