package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.net.LogicMessage;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskBusSequentialSelector implements ITaskBusSelector {

    private List<TaskBus> busList;
    private AtomicInteger index;

    public TaskBusSequentialSelector() {

    }

    @Override
    public void init(List<TaskBus> buses) {
        this.busList = buses;
        index = new AtomicInteger(0);
    }

    @Override
    public TaskBus selectBus(LogicMessage message) {
        TaskBus bus = busList.get(index.intValue());
        int currentIndex = index.incrementAndGet();
        if (currentIndex >= busList.size()) {
            index.set(0);
        }
        return bus;
    }
}
