package org.npc.kungfu.net;

public interface IMessageCoder<T> {

    T encode(LogicMessage message);

    LogicMessage decode(T data);

}
