package org.npc.kungfu.platfame;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskBus implements Callable<Boolean> {

    private final ConcurrentLinkedQueue<LogicMessage> messages;

    public TaskBus() {
        messages = new ConcurrentLinkedQueue<>();
    }

    public void addMessage(LogicMessage message) {
        messages.add(message);
    }

    @Override
    public Boolean call() throws Exception {
        while (!messages.isEmpty()) {
            LogicMessage message = messages.poll();
            System.out.println("run message id:" + message.getId() + " thread:" + Thread.currentThread().getName());
            message.doLogic();
        }
        return true;
    }
}
