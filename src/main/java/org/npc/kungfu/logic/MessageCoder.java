package org.npc.kungfu.logic;

import com.google.gson.*;

import org.npc.kungfu.logic.message.LoginReqMessage;
import org.npc.kungfu.net.IMessageCoder;
import org.npc.kungfu.platfame.LogicMessage;

public class MessageCoder implements IMessageCoder<LogicMessage,String> {

    Gson gson = new Gson();

    public MessageCoder() {

    }

    @Override
    public String encode(LogicMessage message) {
        return null;
    }

    @Override
    public LogicMessage decode(String data) {
        LoginReqMessage message = gson.fromJson(data, LoginReqMessage.class);
        return message;
    }
}
