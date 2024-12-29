package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

public class WebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {

    private final IMessageDispatcher dispatcher;

    WebSocketFrameHandler(IMessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            // 握手完成
            WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
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
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) {
        dispatcher.dispatchMessage(frame, ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
