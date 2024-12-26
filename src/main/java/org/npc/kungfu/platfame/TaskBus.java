package org.npc.kungfu.platfame;

import java.util.LinkedList;
import java.util.concurrent.Callable;

public class TaskBus implements Callable<Boolean> {

    private LinkedList<LogicMessage> messages;

    public TaskBus() {
        messages = new LinkedList<>();
    }

    public void drainToMessages(LinkedList<LogicMessage> messages) {
        this.messages.addAll(messages);
    }

    public void addMessage(LogicMessage message) {
        messages.add(message);
    }

    @Override
    public Boolean call() throws Exception {
        while (!messages.isEmpty()) {
            LogicMessage message = messages.poll();
            System.out.println("run message id:"+message.id+" thread:"+Thread.currentThread().getName());
            message.doLogic();
        }
        return true;
    }
}
