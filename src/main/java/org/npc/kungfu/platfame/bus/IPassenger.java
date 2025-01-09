package org.npc.kungfu.platfame.bus;

public interface IPassenger<T extends ITask> {

    boolean addTask(T task);

    Boolean doActions();

    long getId();

    String description();
}
