package org.npc.kungfu.logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.npc.kungfu.logic.message.MessageEnum;
import org.npc.kungfu.net.IMessageCoder;
import org.npc.kungfu.net.LogicMessage;

/**
 * 编解码器
 */
public class MessageCoder implements IMessageCoder<String> {

    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    public MessageCoder() {

    }

    @Override
    public String encode(LogicMessage message) {
        return gson.toJson(message);
    }

    @Override
    public LogicMessage decode(String data) {
        LogicMessageInner msg = gson.fromJson(data, LogicMessageInner.class);
        MessageEnum messageEnum = MessageEnum.fromValue(msg.getId());
        if (messageEnum == null) {
            throw new RuntimeException("messageEnum is null");
        }

        return gson.fromJson(data, messageEnum.getClazz());
    }

    static class LogicMessageInner {
        @Expose
        private int id;

        public LogicMessageInner() {

        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
