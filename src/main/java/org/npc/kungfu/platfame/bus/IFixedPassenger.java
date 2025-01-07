package org.npc.kungfu.platfame.bus;

public interface IFixedPassenger<T extends ILuggage> extends IPassenger {

    boolean putLuggage(T luggage);

}
