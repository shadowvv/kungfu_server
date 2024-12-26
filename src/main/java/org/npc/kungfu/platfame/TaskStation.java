package org.npc.kungfu.platfame;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TaskStation implements ITaskStation {

    private final int threadNum;
    private final ExecutorService service;
    private final ConcurrentLinkedQueue<TaskBus> tasks;

    public TaskStation(final int threadNum, final String threadName) {
        this.threadNum = threadNum;
        tasks = new ConcurrentLinkedQueue<>();
        service = Executors.newFixedThreadPool(this.threadNum, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName(threadName);
                t.setDaemon(true);
                return t;
            }
        });
    }

    public void addBus(TaskBus task) {
        tasks.add(task);
    }

    public void removeBus(TaskBus task) {
        tasks.remove(task);
    }

    @Override
    public void run() {
        try {
            service.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
