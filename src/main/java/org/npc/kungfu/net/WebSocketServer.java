package org.npc.kungfu.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

    private final int port;
    private final int threadNum;
    private final IMessageDispatcher dispatcher;
    private final IMessageCoder<?, String> coder;

    public WebSocketServer(int port, int threadNum, IMessageDispatcher dispatcher, IMessageCoder<?, String> coder) {
        this.port = port;
        this.threadNum = threadNum;
        this.dispatcher = dispatcher;
        this.coder = coder;
    }

    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(this.threadNum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializerImpl(this.dispatcher, this.coder));

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("WebSocket Server started at port " + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    static class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {
        private final IMessageDispatcher dispatcher;
        private final IMessageCoder<?, String> coder;

        ChannelInitializerImpl(IMessageDispatcher dispatcher, IMessageCoder<?, String> coder) {
            this.dispatcher = dispatcher;
            this.coder = coder;
        }

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
            //
//            pipeline.addLast(new CoderHandler(coder));
            // 自定义业务逻辑处理器
            pipeline.addLast(new WebSocketFrameHandler(this.dispatcher, this.coder));
        }
    }
}

