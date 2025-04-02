package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.base.BaseRespMessage;

public class ErrorMessage extends BaseRespMessage {

    @Expose
    private long reqId;

    @Expose
    private int errorCode;

    public ErrorMessage() {
        super(MessageEnum.ERROR_MESSAGE.getId());
    }

    public ErrorMessage(int reqId, int errorCode) {
        super(9999);
        this.reqId = reqId;
        this.errorCode = errorCode;
    }

    public long getReqId() {
        return reqId;
    }

    public void setReqId(int reqId) {
        this.reqId = reqId;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.PLAYER_MESSAGE;
    }

    @Override
    public String description() {
        return "request:" + reqId + " error:" + errorCode;
    }
}
