package org.npc.kungfu.logic.message;

public class SSStartMatchUpMessage extends BaseMessage {

    public SSStartMatchUpMessage() {
        setId(20001);
    }

    @Override
    public void doLogic() {
//        MatchService.getService().matchUp();
//        MatchService.getService().putMessage(this,0);
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }
}
