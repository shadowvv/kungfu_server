package org.npc.kungfu.platfame.bus;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 业务驱动器
 */
public class StationDriver {

    /**
     * 延迟初始化时间
     */
    private final int initialDelay;
    /**
     * 业务处理间隔
     */
    private final int period;
    /**
     * 业务处理线程池
     */
    private final IBusStation<?, ?, ?> station;
    /**
     * 驱动器线程
     */
    private ScheduledExecutorService executor;

    /**
     * @param station      业务处理线程池
     * @param initialDelay 延迟初始化时间
     * @param period       业务处理间隔
     */
    public StationDriver(IBusStation<?, ?, ?> station, int initialDelay, int period) {
        this.station = station;
        this.initialDelay = initialDelay;
        this.period = period;
    }

    /**
     * 启动启动器
     */
    public void runStation() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                station.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭驱动器
     */
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

}
