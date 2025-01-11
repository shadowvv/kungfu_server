package org.npc.kungfu.platfame.bus;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Bus<T extends IPassenger<V>, V extends ITask> implements IBus<T, V> {

    private final long id;
    private final int busCapacity;
    private final ConcurrentHashMap<Long, T> passengers;
    private final String signature;

    public Bus(long id, String signature) {
        this.busCapacity = 2000;
        this.id = id;
        this.signature = signature;
        this.passengers = new ConcurrentHashMap<>();
    }

    @Override
    public boolean put(T passenger) {
        if (busCapacity <= passengers.size()) {
            //TODO:越界处理
            return false;
        }
        this.passengers.put(passenger.getId(), passenger);
        return true;
    }

    @Override
    public boolean putTask(long passengerId, V Task) {
        T passenger = passengers.get(passengerId);
        if (passenger == null) {
            return false;
        }
        return passenger.addTask(Task);
    }

    @Override
    public Boolean arrived() {
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
        return new LinkedList<>(passengers.values());
    }

    @Override
    public int getPassengerCount() {
        return passengers.size();
    }

    @Override
    public String description() {
        return this.signature;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
