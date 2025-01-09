package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BusSequentialSelector<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> implements IBusSelector<T, V, Z> {

    private List<T> busList;
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
        }
        return bus;
    }
}
