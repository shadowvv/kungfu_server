package org.npc.kungfu.platfame.bus;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SoloPassenger<T extends ITask> implements IPassenger<T> {

    private final long id;
    private final ConcurrentLinkedQueue<T> queue;

    public SoloPassenger(long id) {
        this.id = id;
        queue = new ConcurrentLinkedQueue<>();
    }

    @Override
    public boolean addTask(T task) {
        queue.add(task);
        return true;
    }

    @Override
    public Boolean doActions() {
        while (!queue.isEmpty()) {
            T task = queue.poll();
            if (task != null) {
                task.doAction(this);
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public String description() {
        return "";
    }
}
