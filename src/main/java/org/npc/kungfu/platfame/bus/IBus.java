package org.npc.kungfu.platfame.bus;

public interface IBus<T extends IPassenger<V>, V extends ITask> {

    long getId();

    boolean put(T passenger);

    boolean putTask(long passengerId, V Task);

    Boolean arrived();

    void remove(long passengerId);

    int getPassengerCount();

    String description();

}
