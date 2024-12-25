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

    @Override
    public Boolean call() throws Exception {
        while (!messages.isEmpty()) {
            LogicMessage message = messages.poll();
            message.doLogic();
        }
        return true;
    }
}
