package org.npc.kungfu.platfame.bus;

import java.util.concurrent.Callable;

public interface IBus<T extends IPassenger> extends Callable<Boolean> {

    boolean put(T passenger);

    String getSignature();

    int getPassengerCount();
}
