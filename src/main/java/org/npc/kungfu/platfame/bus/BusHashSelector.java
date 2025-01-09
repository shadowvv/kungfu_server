package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusHashSelector<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> implements IBusSelector<T, V, Z> {

    private List<T> busList;

    public BusHashSelector() {

    }

    @Override
    public void init(List<T> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public T selectBus(V passenger) {
        int index = (int) (passenger.getId() % busList.size());
        return busList.get(index);
    }
}
