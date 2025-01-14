package org.npc.kungfu.platfame.bus;

import java.util.List;

/**
 * 通过处理器id的hash分配业务组
 *
 * @param <T> 业务组
 * @param <V> 业务处理器
 */
public class BusHashSelector<T extends IBus<V, ?>, V extends IPassenger<?>> implements IBusSelector<T, V> {

    /**
     * 业务组
     */
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
