package org.npc.kungfu.platfame.bus;

import java.util.List;

/**
 * 通过组内业务处理器数量分配处理器
 *
 * @param <T> 业务组
 * @param <V> 业务组处理器
 */
public class BusPassengerCountSelector<T extends IBus<V, ?>, V extends IPassenger<?>> implements IBusSelector<T, V> {

    /**
     * 业务组
     */
    private List<T> busList;

    public BusPassengerCountSelector() {
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
