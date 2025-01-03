package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusMessageCountSelector<T extends IPassenger,V extends IBus<T>> implements IBusSelector<T,V> {

    private List<V> busList;

    public BusMessageCountSelector() {
    }

    @Override
    public void init(List<V> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public V selectBus(T passenger) {
        V bus = busList.get(0);
        int count = Integer.MAX_VALUE;
        for (V tempBus : busList) {
            if (tempBus.getPassengerCount() < count) {
                count = tempBus.getPassengerCount();
                bus = tempBus;
            }
        }
        return bus;
    }
}
