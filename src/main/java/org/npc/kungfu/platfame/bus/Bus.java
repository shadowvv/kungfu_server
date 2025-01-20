package org.npc.kungfu.platfame.bus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 业务分组
 *
 * @param <T> 业务处理器
 * @param <V> 具体业务
 */
public class Bus<T extends IPassenger<V>, V extends ITask> implements IBus<T, V> {

    /**
     * 业务分组id
     */
    private final long id;
    /**
     * 分组最大容量
     */
    private final int capacity;
    /**
     * 业务处理器
     */
    private final ConcurrentHashMap<Long, T> passengers;
    /**
     * 描述
     */
    private final String description;

    /**
     * @param id      业务组id
     * @param busName 业务组名字
     */
    public Bus(long id, String busName) {
        this.capacity = 2000;
        this.id = id;
        this.description = busName;
        this.passengers = new ConcurrentHashMap<>();
    }

    @Override
    public boolean put(T passenger) {
        if (capacity <= passengers.size()) {
            System.out.println(description + " bus is full");
            return false;
        }
        this.passengers.put(passenger.getId(), passenger);
        return true;
    }

    @Override
    public boolean put(long passengerId, V Task) {
        T passenger = passengers.get(passengerId);
        if (passenger == null) {
            return false;
        }
        return passenger.put(Task);
    }

    @Override
    public boolean arrived() {
        passengers.forEach((k, v) -> v.doActions());
        return true;
    }

    @Override
    public boolean remove(long passengerId) {
        this.passengers.remove(passengerId);
        return true;
    }

    @Override
    public List<T> getPassengers() {
        return new ArrayList<>(passengers.values());
    }

    @Override
    public int getPassengerCount() {
        return passengers.size();
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
