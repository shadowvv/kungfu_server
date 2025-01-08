package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;

public class SSPlayerChannelInactive extends BaseMessage {

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public void doLogic() {
        Player player = PlayerService.getService().getPlayer(getPlayerId());
        if (player != null) {
            player.onPlayerDisconnect();
        }
    }

    @Override
    public String getDescription() {
        return "SSPlayerChannelInactive playerId" + getPlayerId();
    }
}
