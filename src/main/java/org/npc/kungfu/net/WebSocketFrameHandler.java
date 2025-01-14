package org.npc.kungfu.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 通信handle
 */
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<Object> {

    /**
     * 消息分发器
     */
    private final IMessageDispatcher dispatcher;

    /**
     * @param dispatcher 消息分发器
     */
    WebSocketFrameHandler(IMessageDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        dispatcher.dispatchChannelInactiveMessage(ctx.channel());
        super.channelInactive(ctx);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object frame) {
        dispatcher.dispatchMessage(frame, ctx.channel());
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
        } else if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                System.out.println("读超时，关闭连接");
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 如果需要，也可以在写超时时发送心跳
                System.out.println("写超时");
            } else if (event.state() == IdleState.ALL_IDLE) {
                System.out.println("读写超时");
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
