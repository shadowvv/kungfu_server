package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BusSequentialSelector<T extends IPassenger> implements IBusSelector<T> {

    private List<IBus<T>> busList;
    private AtomicInteger index;

    public BusSequentialSelector() {

    }

    @Override
    public void init(List<IBus<T>> iBuses) {
        this.busList = iBuses;
        index = new AtomicInteger(0);
    }

    @Override
    public IBus<T> selectBus(T passenger) {
        IBus<T> bus = busList.get(index.intValue());
        int currentIndex = index.incrementAndGet();
        if (currentIndex >= busList.size()) {
            index.set(0);
        }
        return bus;
    }
}
