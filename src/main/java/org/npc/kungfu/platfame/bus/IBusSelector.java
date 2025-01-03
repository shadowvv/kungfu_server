package org.npc.kungfu.platfame.bus;

import java.util.List;

public interface IBusSelector<T extends IPassenger,V extends IBus<T>> {

    void init(List<V> buses);

    V selectBus(T passenger);

}
