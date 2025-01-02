package org.npc.kungfu.platfame.bus;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Bus<T extends IPassenger> implements IBus<T>, Callable<Boolean> {

    private final ConcurrentLinkedQueue<T> passengers;
    private final AtomicInteger passengerCount;
    private final String signature;

    public Bus(String signature) {
        this.passengers = new ConcurrentLinkedQueue<>();
        this.passengerCount = new AtomicInteger(0);
        this.signature = signature;
    }

    @Override
    public boolean put(T passenger) {
        this.passengers.add(passenger);
        this.passengerCount.incrementAndGet();
        return true;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

    @Override
    public int getPassengerCount() {
        return passengerCount.get();
    }

    @Override
    public Boolean call() throws Exception {
        while (!passengers.isEmpty()) {
            T passenger = passengers.poll();
            passengerCount.decrementAndGet();
            passenger.doLogic();
        }
        return true;
    }
}
