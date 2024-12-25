package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.*;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    IMessageDispatcher dispatcher;
    IMessageCoder<?,String> coder;

    WebSocketFrameHandler(IMessageDispatcher dispatcher, IMessageCoder<?,String> coder) {
        this.dispatcher = dispatcher;
        this.coder = coder;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手完成
            WebSocketServerProtocolHandler.HandshakeComplete handshake =
                    (WebSocketServerProtocolHandler.HandshakeComplete) evt;

            // 获取握手请求头信息
            String requestUri = handshake.requestUri();
            HttpHeaders headers = handshake.requestHeaders();

            System.out.println("WebSocket handshake complete:");
            System.out.println("Request URI: " + requestUri);

            // 可以根据具体需求执行其他逻辑，比如记录连接信息或通知业务系统
        } else {
            super.userEventTriggered(ctx, evt); // 保留其他事件的处理
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
        if (frame instanceof TextWebSocketFrame) {
            // 处理文本帧
            String request = ((TextWebSocketFrame) frame).text();
            System.out.println("Received: " + request);

            dispatcher.dispatchMessage(coder.decode(request));
            // 回送消息
             ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + request));
        } else if (frame instanceof CloseWebSocketFrame) {
            // 处理关闭帧
            System.out.println("Close frame received");
            ctx.channel().close();
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
