package org.npc.kungfu.platfame.bus;

/**
 * 业务处理器
 *
 * @param <T> 具体业务
 */
public interface IPassenger<T extends ITask> {

    /**
     * @return 业务处理器id
     */
    long getId();

    /**
     * 添加具体业务
     *
     * @param task 业务
     * @return 是否成功
     */
    boolean put(T task);

    /**
     * 处理业务
     */
    void doActions();

    /**
     * 处理器心跳
     */
    //TODO:考虑增加delta time
    void heartbeat();

    /**
     * @return 业务处理器描述
     */
    String description();
}
