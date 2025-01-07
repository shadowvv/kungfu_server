package org.npc.kungfu.platfame.bus;

import java.util.List;

public interface IFixedPassengerBusSelector<T extends IFixedPassenger<Z>, V extends IFixedPassengerBus<T, Z>, Z extends ILuggage> {

    void init(List<V> buses);

    V selectBus(T passenger);

}
