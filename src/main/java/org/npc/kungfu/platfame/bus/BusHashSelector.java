package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusHashSelector<T extends IPassenger> implements IBusSelector<T> {

    private List<IBus<T>> busList;

    public BusHashSelector() {

    }

    @Override
    public void init(List<IBus<T>> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public IBus<T> selectBus(T passenger) {
        int index = (int) (passenger.getId() % busList.size());
        return busList.get(index);
    }
}
