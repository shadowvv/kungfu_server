package org.npc.kungfu.platfame.bus;

import java.util.List;

public class SoloPassengerBus<T extends SoloPassenger<V>, V extends ITask> implements IBus<T, V> {

    private final Bus<T, V> bus;

    public SoloPassengerBus(long id, String signature, T soloPassenger) {
        this.bus = new Bus<>(id, signature);
        this.bus.put(soloPassenger);
    }

    @Override
    public long getId() {
        return bus.getId();
    }

    @Override
    public boolean put(T passenger) {
        throw new UnsupportedOperationException("This method is unsupported");
    }

    @Override
    public boolean putTask(long passengerId, V Task) {
        return bus.putTask(passengerId, Task);
    }

    @Override
    public Boolean arrived() {
        return bus.arrived();
    }

    @Override
    public List<T> getPassengers() {
        return this.bus.getPassengers();
    }

    @Override
    public boolean remove(long passengerId) {
        throw new UnsupportedOperationException("This method is unsupported");
    }

    @Override
    public int getPassengerCount() {
        return bus.getPassengerCount();
    }

    @Override
    public String description() {
        return "";
    }
}
