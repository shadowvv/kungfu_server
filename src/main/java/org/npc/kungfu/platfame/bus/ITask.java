package org.npc.kungfu.platfame.bus;

public interface ITask {

    void doAction(IPassenger<? extends ITask> passenger);

    String description();

}
