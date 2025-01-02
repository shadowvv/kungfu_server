package org.npc.kungfu.platfame.bus;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BusStation<T extends IPassenger> implements IBusStation<T> {

    private final ExecutorService service;
    private final ArrayList<IBus<T>> buses;
    private final ConcurrentHashMap<IBus<T>, Future<?>> futures;
    private final IBusSelector<T> selector;

    public BusStation(final int busCount, final String busName, IBusSelector<T> selector) {
        this.buses = new ArrayList<>();
        for (int i = 0; i < busCount; i++) {
            this.buses.add(new Bus<>(busName));
        }
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
        for (IBus<T> task : buses) {
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
