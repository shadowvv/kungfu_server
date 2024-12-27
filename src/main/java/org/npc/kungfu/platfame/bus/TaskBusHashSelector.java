package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.platfame.LogicMessage;

import java.util.List;

public class TaskBusHashSelector implements ITaskBusSelector{

    private List<TaskBus> busList;

    public TaskBusHashSelector() {

    }

    @Override
    public void init(List<TaskBus> buses) {
        this.busList = buses;
    }

    @Override
    public TaskBus selectBus(LogicMessage message) {
        return busList.get(message.getId()%busList.size());
    }
}
