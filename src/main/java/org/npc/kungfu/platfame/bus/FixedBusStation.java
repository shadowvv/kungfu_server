package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedBusStation<T extends IFixedPassenger<Z>, V extends IFixedPassengerBus<T, Z>, Z extends ILuggage> implements IFixedPassengerBusStation<T, Z> {

    private final ExecutorService service;
    private final List<V> buses;
    private final ConcurrentHashMap<V, Future<?>> futures;
    private final IFixedPassengerBusSelector<T, V, Z> selector;

    public FixedBusStation(final int busCount, final String busName, List<V> buses, IFixedPassengerBusSelector<T, V, Z> selector) {
        this.buses = buses;
        this.selector = selector;
        this.selector.init(this.buses);
        this.futures = new ConcurrentHashMap<>();

        AtomicInteger threadIndex = new AtomicInteger(0);
        service = Executors.newFixedThreadPool(busCount, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName(busName + "_" + threadIndex.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
    }

    @Override
    public void put(T passenger) {
        IFixedPassengerBus<T, Z> bus = this.selector.selectBus(passenger);
        if (bus == null) {
            return;
        }
        bus.put(passenger);
    }

    @Override
    public boolean putLuggage(T passenger, Z luggage) {
        IFixedPassengerBus<T, Z> bus = this.selector.selectBus(passenger);
        if (bus == null) {
            return false;
        }
        return bus.putLuggage(passenger.getId(), luggage);
    }

    @Override
    public void run() {
        for (V task : buses) {
            Future<?> future = futures.get(task);
            if (future == null) {
                Future<Boolean> newFuture = service.submit(task);
                futures.put(task, newFuture);
                continue;
            }

            if (future.isDone()) {
                Future<Boolean> newFuture = service.submit(task);
                futures.put(task, newFuture);
                continue;
            }

            System.out.println("task is busy: " + task.getSignature());
        }
    }
}
