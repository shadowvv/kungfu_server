package org.npc.kungfu.platfame.bus;

import java.util.List;

public interface IBusSelector<T extends IPassenger> {

    void init(List<IBus<T>> buses);

    IBus<T> selectBus(T passenger);

}
