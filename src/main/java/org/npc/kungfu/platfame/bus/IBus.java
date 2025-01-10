package org.npc.kungfu.platfame.bus;

import java.util.List;

public interface IBus<T extends IPassenger<V>, V extends ITask> {

    long getId();

    boolean put(T passenger);

    boolean putTask(long passengerId, V Task);

    Boolean arrived();

    boolean remove(long passengerId);

    int getPassengerCount();

    String description();

    List<T> getPassengers();
}
