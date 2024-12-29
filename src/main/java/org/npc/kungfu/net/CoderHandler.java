package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

import java.util.List;

public class CoderHandler extends MessageToMessageCodec<WebSocketFrame, LogicMessage> {

    private final IMessageCoder<String> coder;

    public CoderHandler(IMessageCoder<String> coder) {
        this.coder = coder;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, LogicMessage msg, List<Object> out) throws Exception {
        String text = coder.encode(msg);
        WebSocketFrame frame = new TextWebSocketFrame(text);
        out.add(frame);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, WebSocketFrame frame, List<Object> out) throws Exception {
        if (frame instanceof TextWebSocketFrame) {
            // 处理文本帧
            String request = ((TextWebSocketFrame) frame).text();
            out.add(coder.decode(request));
        } else if (frame instanceof CloseWebSocketFrame) {
            // 处理关闭帧
            System.out.println("Close frame received");
            ctx.channel().close();
        }
    }
}
