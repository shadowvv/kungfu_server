package org.npc.kungfu.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    private final int port;

    public WebSocketServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();

                            // HTTP 编解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 写入大块数据支持
                            pipeline.addLast(new ChunkedWriteHandler());
                            // 聚合 HTTP 消息
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            // WebSocket 协议处理器，自动完成握手
                            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

                            // 自定义业务逻辑处理器
                            pipeline.addLast(new WebSocketFrameHandler());
                        }
                    });

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("WebSocket Server started at port " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new WebSocketServer(8080).start();
    }

    // 自定义 WebSocket 处理器
    static class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {
            if (frame instanceof TextWebSocketFrame) {
                // 处理文本帧
                String request = ((TextWebSocketFrame) frame).text();
                System.out.println("Received: " + request);

                // 回送消息
                ctx.channel().writeAndFlush(new TextWebSocketFrame("Server received: " + request));
            } else if (frame instanceof BinaryWebSocketFrame) {
                // 处理二进制帧
                System.out.println("Binary frame received");
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
}

