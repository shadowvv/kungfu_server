package org.npc.kungfu.net;

/**
 * 消息编码器
 *
 * @param <T> 数据类型
 */
public interface IMessageCoder<T> {

    /**
     * 编码
     *
     * @param message 消息
     * @return 数据
     */
    T encode(LogicMessage message);

    /**
     * 解码
     *
     * @param data 数据
     * @return 消息
     */
    LogicMessage decode(T data);

}
