package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusHashSelector<T extends IPassenger,V extends IBus<T>> implements IBusSelector<T,V> {

    private List<V> busList;

    public BusHashSelector() {

    }

    @Override
    public void init(List<V> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public V selectBus(T passenger) {
        int index = (int) (passenger.getId() % busList.size());
        return busList.get(index);
    }
}
