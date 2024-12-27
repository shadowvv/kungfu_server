package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.match.MatchService;

public class SSStartMatchUpMessage extends BaseMessage {

    @Override
    public void doLogic() {
        MatchService.getService().matchUp();
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }
}
