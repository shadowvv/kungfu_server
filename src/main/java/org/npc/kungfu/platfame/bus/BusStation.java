package org.npc.kungfu.platfame.bus;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BusStation<T extends IPassenger,V extends IBus<T>> implements IBusStation<T> {

    private final ExecutorService service;
    private final List<V> buses;
    private final ConcurrentHashMap<V, Future<?>> futures;
    private final IBusSelector<T,V> selector;

    public BusStation(final int busCount, final String busName, List<V> buses, IBusSelector<T,V> selector) {
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
        IBus<T> bus = selector.selectBus(passenger);
        if (bus != null) {
            bus.put(passenger);
        }
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

//            System.out.println("task is busy: " + task.getSignature());
        }
    }
}
