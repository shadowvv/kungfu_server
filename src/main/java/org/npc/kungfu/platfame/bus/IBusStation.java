package org.npc.kungfu.platfame.bus;

public interface IBusStation<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> extends Runnable {

    boolean put(T bus);

    boolean put(V passenger);

    boolean put(long passengerId, Z task);

    boolean remove(V passenger);
}
