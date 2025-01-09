package org.npc.kungfu.platfame.bus;

import java.util.concurrent.ConcurrentLinkedQueue;

public class SoloPassenger<T extends ITask> implements IPassenger<T> {

    private final ConcurrentLinkedQueue<T> queue;

    public SoloPassenger() {
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
                task.doAction();
            }
        }
        return Boolean.TRUE;
    }

    @Override
    public long getId() {
        return 0;
    }

    @Override
    public String description() {
        return "";
    }
}
