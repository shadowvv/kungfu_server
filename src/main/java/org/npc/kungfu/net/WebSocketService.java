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
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * websocket 通信服务
 */
public class WebSocketService {

    /**
     * 通信端口
     */
    public static final int DEFAULT_PORT = 8080;
    private static final int READ_IDLE_TIME = 30;
    private static final int WRITE_IDLE_TIME = 0;
    private static final int ALL_IDLE_TIME = 0;

    /**
     * 通信端口
     */
    private final int port;
    /**
     * io通信线程数量
     */
    private final int threadNum;
    /**
     * 消息分发器
     */
    private final IMessageDispatcher dispatcher;
    /**
     * 消息编码器
     */
    private final IMessageCoder<String> coder;

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    /**
     * @param port       通信端口
     * @param threadNum  io通信线程数量
     * @param dispatcher 消息分发器
     * @param coder      消息编码器
     */
    public WebSocketService(int port, int threadNum, IMessageDispatcher dispatcher, IMessageCoder<String> coder) {
        this.port = port;
        this.threadNum = threadNum;
        this.dispatcher = dispatcher;
        this.coder = coder;
    }

    /**
     * 启动服务
     *
     * @throws InterruptedException 抛出的异常
     */
    public void start() throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup(this.threadNum);
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializerImpl(this.dispatcher, this.coder));

            ChannelFuture future = bootstrap.bind(port).sync();
            logger.info("Client Idle parameters - READ_IDLE_TIME: {}, WRITE_IDLE_TIME: {}, ALL_IDLE_TIME: {}", READ_IDLE_TIME, WRITE_IDLE_TIME, ALL_IDLE_TIME);
            logger.info("WebSocket Server started at port {}", port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 通道初始化实现
     */
    static class ChannelInitializerImpl extends ChannelInitializer<SocketChannel> {

        /**
         * 消息分发器
         */
        private final IMessageDispatcher dispatcher;
        /**
         * 消息编码器
         */
        private final IMessageCoder<String> coder;

        /**
         * @param dispatcher 消息分发器
         * @param coder      消息编码器
         */
        ChannelInitializerImpl(IMessageDispatcher dispatcher, IMessageCoder<String> coder) {
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

            pipeline.addLast(new IdleStateHandler(READ_IDLE_TIME, WRITE_IDLE_TIME, ALL_IDLE_TIME));
            //
            pipeline.addLast(new CoderHandler(coder));
            // 自定义业务逻辑处理器
            pipeline.addLast(new WebSocketFrameHandler(this.dispatcher));
        }
    }
}

