package org.npc.kungfu.platfame.bus;

import java.util.concurrent.ConcurrentHashMap;

public class FixedPassengerBus<T extends IFixedPassenger<V>, V extends ILuggage> implements IFixedPassengerBus<T, V> {

    private final int busCapacity;
    private final ConcurrentHashMap<Integer, T> passengers;
    private final String signature;

    public FixedPassengerBus(String signature) {
        this.passengers = new ConcurrentHashMap<>();
        this.signature = signature;
        this.busCapacity = 2000;
    }

    @Override
    public boolean putLuggage(int passengerId, V luggage) {
        T passenger = passengers.get(passengerId);
        if (passenger == null) {
            return false;
        }
        return passenger.putLuggage(luggage);
    }

    @Override
    public void remove(T passenger) {
        passengers.remove(passenger.getId());
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
    public String getSignature() {
        return signature;
    }

    @Override
    public int getPassengerCount() {
        return passengers.size();
    }

    @Override
    public Boolean call() throws Exception {
        passengers.forEach((key, value) -> {
            value.doLogic();
        });
        return true;
    }
}
