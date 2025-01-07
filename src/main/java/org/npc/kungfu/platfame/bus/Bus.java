package org.npc.kungfu.platfame.bus;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Bus<T extends IPassenger> implements IBus<T> {

    private final int busCapacity;
    private final ConcurrentLinkedQueue<T> passengers;
    private final AtomicInteger passengerCount;
    private final String signature;

    public Bus(String signature) {
        this.passengers = new ConcurrentLinkedQueue<>();
        this.passengerCount = new AtomicInteger(0);
        this.signature = signature;
        this.busCapacity = 2000;
    }

    @Override
    public boolean put(T passenger) {
        if (busCapacity <= passengerCount.get()) {
            //TODO:越界处理
            return false;
        }
        this.passengers.offer(passenger);
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
            try {
                passenger.doLogic();
            } catch (Exception e) {
                System.out.println("do logic exception:" + e.getMessage() + " message:" + passenger.getDescription());
                throw new RuntimeException(e);
            }
        }
        return true;
    }
}
