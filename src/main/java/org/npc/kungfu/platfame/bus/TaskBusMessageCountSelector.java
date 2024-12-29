package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.net.LogicMessage;

import java.util.List;

public class TaskBusMessageCountSelector implements ITaskBusSelector {

    private List<TaskBus> busList;

    public TaskBusMessageCountSelector() {
    }

    @Override
    public void init(List<TaskBus> buses) {
        this.busList = buses;
    }

    @Override
    public TaskBus selectBus(LogicMessage message) {
        TaskBus bus = busList.get(0);
        int count = Integer.MAX_VALUE;
        for (TaskBus tempBus : busList) {
            if (tempBus.getMessageCount() < count) {
                count = tempBus.getMessageCount();
                bus = tempBus;
            }
        }
        return bus;
    }
}
