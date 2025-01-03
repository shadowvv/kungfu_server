package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BusSequentialSelector<T extends IPassenger,V extends IBus<T>> implements IBusSelector<T,V> {

    private List<V> busList;
    private AtomicInteger index;

    public BusSequentialSelector() {

    }

    @Override
    public void init(List<V> buses) {
        this.busList = buses;
        index = new AtomicInteger(0);
    }

    @Override
    public V selectBus(T passenger) {
        V bus = busList.get(index.intValue());
        int currentIndex = index.incrementAndGet();
        if (currentIndex >= busList.size()) {
            index.set(0);
        }
        return bus;
    }
}
