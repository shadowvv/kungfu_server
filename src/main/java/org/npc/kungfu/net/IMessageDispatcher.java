package org.npc.kungfu.net;

import io.netty.channel.Channel;

/**
 * 通信消息分发器
 */
public interface IMessageDispatcher {

    /**
     * 分发消息
     *
     * @param message       消息
     * @param senderChannel 发送消息的通道
     */
    void dispatchMessage(Object message, Channel senderChannel);

    /**
     * 通知断开连接
     *
     * @param channel 断开的通道
     */
    void dispatchChannelInactiveMessage(Channel channel);
}
