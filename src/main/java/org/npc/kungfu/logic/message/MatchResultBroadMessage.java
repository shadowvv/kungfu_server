package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

import java.util.List;

public class MatchResultBroadMessage extends BaseRespMessage {

    @Expose
    List<PlayerInfoMessage> playerInfoList;

    public MatchResultBroadMessage() {
        super(MessageEnum.MATCH_RESULT_BROAD.getId());
    }

    @Override
    public String description() {
        return "";
    }

    public List<PlayerInfoMessage> getPlayerInfoList() {
        return playerInfoList;
    }

    public void setPlayerInfoList(List<PlayerInfoMessage> playerInfoList) {
        this.playerInfoList = playerInfoList;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.MATCH_MESSAGE;
    }
}
