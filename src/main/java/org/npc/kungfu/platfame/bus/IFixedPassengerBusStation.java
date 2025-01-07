package org.npc.kungfu.platfame.bus;

public interface IFixedPassengerBusStation<T extends IFixedPassenger<V>, V extends ILuggage> extends IBusStation<T>, Runnable {

    void put(T passenger);

    boolean putLuggage(T passenger, V luggage);

}
