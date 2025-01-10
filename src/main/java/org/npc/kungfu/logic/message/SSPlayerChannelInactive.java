package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;

public class SSPlayerChannelInactive extends BasePlayerMessage {

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public void doAction(Player player) {
        player.onPlayerDisconnect();
    }

    @Override
    public String description() {
        return "SSPlayerChannelInactive playerId" + getPlayerId();
    }
}
