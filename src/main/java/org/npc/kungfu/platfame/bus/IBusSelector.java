package org.npc.kungfu.platfame.bus;

import java.util.List;

public interface IBusSelector<T extends IBus<V, Z>, V extends IPassenger<Z>, Z extends ITask> {

    void init(List<T> buses);

    T selectBus(V passenger);

}
