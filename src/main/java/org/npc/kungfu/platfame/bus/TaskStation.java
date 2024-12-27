package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.platfame.LogicMessage;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskStation implements ITaskStation {

    private final int threadNum;
    private final ExecutorService service;
    private final ArrayList<TaskBus> tasks;
    private final ConcurrentHashMap<TaskBus, Future<?>> futures;
    private final ITaskBusSelector selector;

    public TaskStation(final int threadNum, final String threadName,final ITaskBusSelector selector) {
        this.threadNum = threadNum;
        this.tasks = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            this.tasks.add(new TaskBus(threadName+"_"+i));
        }
        this.selector = selector;
        this.selector.init(tasks);
        this.futures = new ConcurrentHashMap<>();

        AtomicInteger threadIndex = new AtomicInteger(0);
        service = Executors.newFixedThreadPool(this.threadNum, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName(threadName+"_"+threadIndex.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        });
    }

    public void putMessage(LogicMessage message) {
        TaskBus task = selector.selectBus(message);
        if (task != null) {
            task.addMessage(message);
        }
    }

    @Override
    public void run() {
        for (TaskBus task : tasks) {
            Future<?> future = futures.get(task);
            if (future == null) {
                Future<Boolean> newFuture = service.submit(task);
                futures.put(task, newFuture);
                continue;
            }

            if (future.isDone()) {
                Future<Boolean> newFuture = service.submit(task);
                futures.put(task, newFuture);
                continue;
            }

            System.out.println("task is busy: " + task.getSignature());
        }
    }
}
