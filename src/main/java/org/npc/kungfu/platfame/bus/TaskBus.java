package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.platfame.LogicMessage;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskBus implements Callable<Boolean> {

    private final ConcurrentLinkedQueue<LogicMessage> messages;
    private final AtomicInteger messageCount;
    private final String signature;

    public TaskBus(String signature) {
        messages = new ConcurrentLinkedQueue<>();
        messageCount = new AtomicInteger(0);
        this.signature = signature;
    }

    public void addMessage(LogicMessage message) {
        messages.add(message);
        messageCount.incrementAndGet();
    }

    @Override
    public Boolean call() throws Exception {
        while (!messages.isEmpty()) {
            LogicMessage message = messages.poll();
            messageCount.decrementAndGet();
            System.out.println("run message id:" + message.getId() + " thread:" + Thread.currentThread().getName());
            message.doLogic();
        }
        return true;
    }

    public int getMessageCount() {
        return messageCount.get();
    }

    public String getSignature() {
        return signature;
    }
}
