package org.npc.kungfu.platfame.bus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 业务驱动器
 * 用于调度和执行业务处理任务，支持定时任务和线程池管理。
 */
public class StationDriver {

    private static final Logger logger = LoggerFactory.getLogger(StationDriver.class);

    /**
     * 延迟初始化时间（毫秒）
     */
    private final int initialDelay;

    /**
     * 业务处理间隔时间（毫秒）
     */
    private final int period;

    /**
     * 业务处理任务
     */
    private final IBusStation<?, ?, ?> station;

    /**
     * 驱动器线程池
     */
    private ScheduledExecutorService executor;

    /**
     * 构造函数
     *
     * @param station      业务处理任务
     * @param initialDelay 延迟初始化时间（毫秒）
     * @param period       业务处理间隔时间（毫秒）
     * @throws IllegalArgumentException 如果参数不合法
     */
    public StationDriver(IBusStation<?, ?, ?> station, int initialDelay, int period) {
        if (station == null) {
            throw new IllegalArgumentException("task 不能为空");
        }
        if (initialDelay < 0 || period < 0) {
            throw new IllegalArgumentException("initialDelay 和 period 必须为非负数");
        }
        this.station = station;
        this.initialDelay = initialDelay;
        this.period = period;
    }

    /**
     * 启动驱动器
     * 创建单线程的调度线程池，并定期执行业务处理任务。
     */
    public void runStation() {
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            try {
                station.run();
            } catch (Exception e) {
                logger.error("业务处理任务执行失败", e);
                // 可以增加异常通知逻辑
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    /**
     * 关闭驱动器
     * 优雅关闭线程池，设置超时时间以避免长时间等待。
     */
    public void shutdown() {
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown(); // 尝试优雅关闭
            try {
                if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { // 设置超时时间
                    executor.shutdownNow(); // 强制关闭
                }
            } catch (InterruptedException e) {
                logger.warn("线程池关闭被中断", e);
                executor.shutdownNow();
            }
        }
    }

    /**
     * 检查驱动器是否正在运行
     *
     * @return 如果线程池未关闭，则返回 true；否则返回 false
     */
    public boolean isRunning() {
        return executor != null && !executor.isShutdown();
    }
}
