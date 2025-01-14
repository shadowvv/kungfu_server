package org.npc.kungfu.platfame.bus;

import java.util.List;

/**
 * 业务分发器
 *
 * @param <T> 业务组
 * @param <V> 业务处理器
 */
public interface IBusSelector<T extends IBus<V, ?>, V extends IPassenger<?>> {

    /**
     * 初始化分发器
     *
     * @param buses 业务组
     */
    void init(List<T> buses);

    /**
     * 选择业务组
     *
     * @param passenger 业务分发器
     * @return 业务组
     */
    T selectBus(V passenger);

}
