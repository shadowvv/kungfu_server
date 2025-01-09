package org.npc.kungfu.platfame.bus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class StationDriver {

    private final int initialDelay;
    private final int period;

    IBusStation<?, ?, ?> station;
    ScheduledExecutorService executor;

    public StationDriver(IBusStation<?, ?, ?> station, int initialDelay, int period) {
        this.station = station;
        this.initialDelay = initialDelay;
        this.period = period;
    }

    public void runStation() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(station, initialDelay, period, TimeUnit.MILLISECONDS);
    }

}
