package org.npc.kungfu.platfame.bus;

public interface IFixedPassengerBus<T extends IFixedPassenger<V>, V extends ILuggage> extends IBus<T> {

    boolean putLuggage(int passengerId, V luggage);


    void remove(T passenger);
}
