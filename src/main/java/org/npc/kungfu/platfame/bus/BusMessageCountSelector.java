package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusMessageCountSelector<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> implements IBusSelector<T, V, Z> {

    private List<T> busList;

    public BusMessageCountSelector() {
    }

    @Override
    public void init(List<T> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public T selectBus(V passenger) {
        T bus = busList.get(0);
        int count = Integer.MAX_VALUE;
        for (T tempBus : busList) {
            if (tempBus.getPassengerCount() < count) {
                count = tempBus.getPassengerCount();
                bus = tempBus;
            }
        }
        return bus;
    }
}
