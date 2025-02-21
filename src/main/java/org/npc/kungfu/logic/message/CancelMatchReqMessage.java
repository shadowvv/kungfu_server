package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.message.base.BaseClientMatchMessage;

public class CancelMatchReqMessage extends BaseClientMatchMessage {

    public CancelMatchReqMessage() {
        super(3001);
    }

    @Override
    public void doAction(MatchPool matchPool) {
        Player player = PlayerService.getService().getPlayer(getPlayerId());
        if (player == null) {
            getSenderChannel().writeAndFlush(new ErrorMessage(2001, ErrorCode.SYSTEM_ERROR.getCode()));
            getSenderChannel().close();
            return;
        }
        if (player.isInBattle()) {
            getSenderChannel().writeAndFlush(new ErrorMessage(2001, ErrorCode.PLAYER_IN_BATTLE.getCode()));
            return;
        }
        if (!player.isInMatch()) {
            getSenderChannel().writeAndFlush(new ErrorMessage(2001, ErrorCode.PLAYER_NOT_IN_MATCH.getCode()));
            return;
        }
        if (matchPool.cancelMatch(player.getRoleId())) {
            player.exitMatch();
            player.sendMessage(new CancelMatchRespMessage());
        }
    }

    @Override
    public String description() {
        return "CancelMatchReqMessage playerId:" + getPlayerId();
    }
}
