package org.npc.kungfu.platfame.bus;

import java.util.List;

public class BusMessageCountSelector<T extends IPassenger> implements IBusSelector<T> {

    private List<IBus<T>> busList;

    public BusMessageCountSelector() {
    }

    @Override
    public void init(List<IBus<T>> iBuses) {
        this.busList = iBuses;
    }

    @Override
    public IBus<T> selectBus(T passenger) {
        IBus<T> bus = busList.get(0);
        int count = Integer.MAX_VALUE;
        for (IBus<T> tempBus : busList) {
            if (tempBus.getPassengerCount() < count) {
                count = tempBus.getPassengerCount();
                bus = tempBus;
            }
        }
        return bus;
    }
}
