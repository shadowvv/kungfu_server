package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 顺序分配业务处理器到业务组
 *
 * @param <T> 业务组
 * @param <V> 业务处理器
 */
public class BusSequentialSelector<T extends IBus<V, ?>, V extends IPassenger<?>> implements IBusSelector<T, V> {

    /**
     * 业务组
     */
    private List<T> busList;
    /**
     * 分配的业务组索引
     */
    private AtomicInteger index;

    public BusSequentialSelector() {

    }

    @Override
    public void init(List<T> buses) {
        this.busList = buses;
        index = new AtomicInteger(0);
    }

    @Override
    public T selectBus(V passenger) {
        T bus = busList.get(index.intValue());
        int currentIndex = index.incrementAndGet();
        if (currentIndex >= busList.size()) {
            index.set(0);
            index.compareAndSet(0, currentIndex);
        }
        return bus;
    }
}
