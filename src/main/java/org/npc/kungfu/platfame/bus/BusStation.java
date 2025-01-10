package org.npc.kungfu.platfame.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BusStation<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> implements IBusStation<T, V, Z> {

    private final ExecutorService service;
    private final List<T> buses;
    private final ConcurrentHashMap<Long, T> passengerIdBusMap;
    private final ConcurrentHashMap<T, CompletableFuture<Boolean>> futures;
    private final IBusSelector<T, V, Z> selector;

    public BusStation(final int threadNum, final String busName, IBusSelector<T, V, Z> selector) {
        this.selector = selector;
        this.buses = new ArrayList<>();
        this.selector.init(this.buses);

        this.passengerIdBusMap = new ConcurrentHashMap<>();
        this.futures = new ConcurrentHashMap<>();

        AtomicInteger threadIndex = new AtomicInteger(0);
        service = Executors.newFixedThreadPool(threadNum, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName(busName + "_" + threadIndex.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
    }

    @Override
    public boolean put(T bus) {
        buses.add(bus);
        if (bus.getPassengerCount() > 0) {
            List<V> passengers = bus.getPassengers();
            for (V passenger : passengers) {
                bind(bus, passenger);
            }
        }
        return true;
    }

    @Override
    public boolean put(V passenger) {
        T bus = selector.selectBus(passenger);
        if (bus == null) {
            return false;
        }
        bus.put(passenger);
        bind(bus, passenger);
        return true;
    }

    @Override
    public boolean put(long passengerId, Z task) {
        IBus<V, Z> bus = passengerIdBusMap.get(passengerId);
        if (bus == null) {
            return false;
        }
        return bus.putTask(passengerId, task);
    }

    private void bind(T bus, V passenger) {
        passengerIdBusMap.put(passenger.getId(), bus);
    }

    @Override
    public boolean remove(V passenger) {
        T bus = passengerIdBusMap.get(passenger.getId());
        if (bus == null) {
            return false;
        }
        return bus.remove(passenger.getId());
    }

    @Override
    public void run() {
        for (T bus : buses) {
            CompletableFuture<Boolean> future = futures.get(bus);
            if (future == null || future.isDone()) {
                CompletableFuture<Boolean> newFuture = CompletableFuture.supplyAsync(() -> {
                    try {
                        return bus.arrived();
                    } catch (Exception e) {
                        System.out.println("Task execution exception: " + e.getMessage());
                        return false;
                    }
                }, service);
                futures.put(bus, newFuture);
            }
        }
    }
}
