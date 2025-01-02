package org.npc.kungfu.platfame.bus;

public interface IBusStation<T extends IPassenger> extends Runnable {

    void put(T passenger);

}
