package org.npc.kungfu.platfame.bus;

/**
 * 业务调度器
 *
 * @param <T> 业务分组
 * @param <V> 业务处理器
 * @param <Z> 具体业务类
 */
public interface IBusStation<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> extends Runnable {

    /**
     * 停止调度器
     */
    void stop();

    /**
     * 添加业务组
     *
     * @param bus 业务组
     * @return 是否成功
     */
    boolean put(T bus);

    /**
     * 移除业务组
     *
     * @param bus 业务组
     * @return 是否成功
     */
    boolean remove(T bus);

    /**
     * 添加业务处理器
     *
     * @param passenger 业务处理器
     * @return 是否成功
     */
    boolean put(V passenger);

    /**
     * 移除业务处理器
     *
     * @param passenger 业务处理器
     * @return 是否成功
     */
    boolean remove(V passenger);

    /**
     * 添加具体业务到业务处理器
     *
     * @param passengerId 业务处理器id
     * @param task        具体业务类
     * @return 是否添加成功
     */
    boolean put(long passengerId, Z task);

    /**
     * @return 业务调度器描述
     */
    String description();
}
