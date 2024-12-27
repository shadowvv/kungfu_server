package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.platfame.LogicMessage;

import java.util.List;

public interface ITaskBusSelector {

    void init(List<TaskBus> buses);

    TaskBus selectBus(LogicMessage message);

}
