package org.npc.kungfu.net;

public interface IMessageCoder<T,V> {

    V encode(T message);

    T decode(V data);

}
