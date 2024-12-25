package org.npc.kungfu.platfame;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class TaskStation implements ITaskStation {

    private final int threadNum;

    ExecutorService service;
    List<TaskBus> tasks;

    public TaskStation(int threadNum) {
        this.threadNum = threadNum;
        tasks = new ArrayList<TaskBus>();
        service = Executors.newFixedThreadPool(threadNum, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
    }

    public void addBus(TaskBus task) {
        tasks.add(task);
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
