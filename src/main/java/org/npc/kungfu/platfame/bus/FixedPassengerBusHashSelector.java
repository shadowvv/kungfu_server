package org.npc.kungfu.platfame.bus;

import java.util.List;

public class FixedPassengerBusHashSelector<T extends IFixedPassenger<Z>, V extends IFixedPassengerBus<T, Z>, Z extends ILuggage> implements IFixedPassengerBusSelector<T, V, Z> {

    private List<V> busList;

    @Override
    public void init(List<V> buses) {
        this.busList = buses;
    }

    @Override
    public V selectBus(T passenger) {
        int index = (int) (passenger.getId() % busList.size());
        return busList.get(index);
    }
}
