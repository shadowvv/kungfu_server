package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.handler.codec.http.websocketx.*;

import java.util.List;

/**
 * 编解码handle
 */
public class CoderHandler extends MessageToMessageCodec<WebSocketFrame, LogicMessage> {

    /**
     * string编解码器
     */
    private final IMessageCoder<String> coder;

    /**
     * @param coder string编解码器
     */
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
        } else if (frame instanceof PingWebSocketFrame) {
            // 收到 Ping，返回 Pong
            System.out.println("收到 Ping，发送 Pong");
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
        } else if (frame instanceof PongWebSocketFrame) {
            // 收到 Pong，心跳正常
            System.out.println("收到 Pong");
        } else if (frame instanceof CloseWebSocketFrame) {
            // 关闭连接
            System.out.println("收到关闭帧，关闭连接");
            ctx.close();
        }
    }
}
