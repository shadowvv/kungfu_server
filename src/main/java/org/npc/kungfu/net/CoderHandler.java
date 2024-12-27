package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import org.npc.kungfu.platfame.LogicMessage;

import java.util.List;

public class CoderHandler extends MessageToMessageCodec<String, LogicMessage> {

    private final IMessageCoder<?, String> coder;

    public CoderHandler(IMessageCoder<?, String> coder) {
        this.coder = coder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogicMessage msg, List<Object> out) throws Exception {
//        out.add(coder.encode(msg));
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, String msg, List<Object> out) throws Exception {
        out.add(coder.decode(msg));
    }
}
