package org.npc.kungfu.net;

import io.netty.channel.Channel;

public interface IMessageDispatcher {

    void dispatchMessage(Object message, Channel senderChannel);

    void dispatchChannelInactiveMessage(Channel channel);
}
