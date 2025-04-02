package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.message.base.BaseServerPlayerMessage;

public class SSPlayerChannelInactive extends BaseServerPlayerMessage {

    private final long playerId;

    public SSPlayerChannelInactive(long playerId) {
        super(MessageEnum.SS_PLAYER_CHANNEL_INACTIVE.getId());
        this.playerId = playerId;
    }

    public long getPlayerId() {
        return playerId;
    }

    @Override
    public void doAction(Player player) {
        player.onPlayerDisconnect();
    }

    @Override
    public String description() {
        return "SSPlayerChannelInactive playerId" + playerId;
    }
}
