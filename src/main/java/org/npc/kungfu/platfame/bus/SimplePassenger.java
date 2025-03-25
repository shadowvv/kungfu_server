package org.npc.kungfu.platfame.bus;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 简单业务处理器
 *
 * @param <T> 具体业务
 */
public class SimplePassenger<T extends ITask> implements IPassenger<T> {

    /**
     * 处理器id
     */
    private final long id;
    /**
     * 业务链表
     */
    private final ConcurrentLinkedQueue<T> queue;

    /**
     * @param id 业务id
     */
    public SimplePassenger(long id) {
        this.id = id;
        queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean put(T task) {
        queue.add(task);
        return true;
    }

    @Override
    public void doActions() {
        while (!queue.isEmpty()) {
            T task = queue.poll();
            if (task != null) {
                task.doAction(this);
            }
        }
        heartbeat();
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String description() {
        return "SimplePassenger id: " + this.id;
    }
}
