package org.npc.kungfu.platfame.bus;

import java.util.List;

/**
 * 业务分组
 *
 * @param <T> 业务处理器
 * @param <V> 具体业务
 */
public interface IBus<T extends IPassenger<V>, V extends ITask> {

    /**
     * @return 分组id
     */
    long getId();

    /**
     * 添加业务处理器
     *
     * @param passenger 业务处理器
     * @return 是否成功
     */
    boolean put(T passenger);

    /**
     * 移除业务处理器
     *
     * @param passengerId 业务处理器id
     * @return 是否成功
     */
    boolean remove(long passengerId);

    /**
     * 添加具体业务
     *
     * @param passengerId 业务处处理器id
     * @param Task        具体业务
     * @return 是否成功
     */
    boolean put(long passengerId, V Task);

    /**
     * 执行业务
     *
     * @return 是否执行成功
     */
    boolean arrived();

    /**
     * @return 处理器列表
     */
    List<T> getPassengers();

    /**
     * @return 处理器数量
     */
    int getPassengerCount();

    /**
     * @return 业务组描述
     */
    String description();
}
