package org.npc.kungfu.platfame.bus;

public interface IPassenger<T extends ITask> {

    boolean addTask(T task);

    boolean doActions();

    void heartbeat();

    long getId();

    String description();
}
