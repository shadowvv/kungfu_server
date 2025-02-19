package org.npc.kungfu.platfame.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 业务调度器
 *
 * @param <T> 业务分组
 * @param <V> 业务处理器
 * @param <Z> 具体业务类
 */
public class BusStation<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> implements IBusStation<T, V, Z> {

    /**
     * 业务执行线程池
     */
    private final ExecutorService service;
    /**
     * 线程池内线程数量
     */
    private final int threadNum;
    /**
     * 业务分组
     */
    private final CopyOnWriteArrayList<T> buses;
    /**
     * 业务处理器与业务分组id的映射
     */
    private final ConcurrentHashMap<Long, T> passengerIdBusMap;
    /**
     * 业务分组组内业务的执行结果
     */
    private final ConcurrentHashMap<T, CompletableFuture<Boolean>> futures;
    /**
     * 业务分发器
     */
    private final IBusSelector<T, V> selector;
    /**
     * 业务调度器描述
     */
    private final String description;


    /**
     * @param threadNum   线程池大小
     * @param stationName 调度器名
     * @param selector    业务分发器
     */
    public BusStation(final int threadNum, final String stationName, IBusSelector<T, V> selector) {
        this(threadNum, stationName, selector, new ArrayList<>());
    }

    /**
     * @param threadNum   线程池大小
     * @param stationName 调度器名
     * @param selector    业务分发器
     * @param buses       已有的分组
     */
    public BusStation(final int threadNum, final String stationName, final IBusSelector<T, V> selector, List<T> buses) {
        this.threadNum = threadNum;
        this.selector = selector;
        this.passengerIdBusMap = new ConcurrentHashMap<>();
        this.futures = new ConcurrentHashMap<>();
        this.description = "busStation:" + stationName + " threadNum:" + threadNum;

        if (buses.size() >= this.threadNum) {
            System.out.println(description + " station is full");
        }
        this.buses = new CopyOnWriteArrayList<>(buses);
        this.selector.init(this.buses);

        AtomicInteger threadIndex = new AtomicInteger(0);
        service = Executors.newFixedThreadPool(threadNum, r -> {
            Thread t = new Thread(r);
            t.setName(stationName + "_" + threadIndex.getAndIncrement());
            t.setDaemon(true);
            return t;
        });
    }

    @Override
    public void stop() {
        if (service != null && !service.isShutdown()) {
            service.shutdown();
        }
    }

    @Override
    public boolean put(T bus) {
        if (buses.size() >= this.threadNum) {
            System.out.println(description + " station is full");
            return false;
        }
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
    public boolean remove(T bus) {
        if (bus.getPassengerCount() > 0) {
            return false;
        }
        return buses.remove(bus);
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
        return bus.put(passengerId, task);
    }

    /**
     * 创建映射关系
     *
     * @param bus       业务组
     * @param passenger 业务处理器
     */
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
                        return bus.arrived();
//                    try {
//                    } catch (Exception e) {
//                        System.out.println("Task execution exception: " + e.getMessage());
//                        return false;
//                    }
                }, service);
                futures.put(bus, newFuture);
            }
        }
    }

    @Override
    public String description() {
        return this.description;
    }
}
