package org.npc.kungfu.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.npc.kungfu.logic.message.MessageEnum;
import org.npc.kungfu.net.IMessageCoder;
import org.npc.kungfu.platfame.LogicMessage;

/**
 * 编解码器
 */
public class MessageCoder implements IMessageCoder<LogicMessage, String> {

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public MessageCoder() {

    }

    @Override
    public String encode(LogicMessage message) {
        return gson.toJson(message);
    }

    @Override
    public LogicMessage decode(String data) {
        LogicMessage msg = gson.fromJson(data, LogicMessage.class);
        MessageEnum messageEnum = MessageEnum.fromValue(msg.getId());
        if (messageEnum == null) {
            throw new RuntimeException("messageEnum is null");
        }

        return gson.fromJson(data, messageEnum.getClazz());
    }
}
