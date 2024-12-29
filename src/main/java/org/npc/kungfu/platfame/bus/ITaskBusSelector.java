package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.net.LogicMessage;

import java.util.List;

public interface ITaskBusSelector {

    void init(List<TaskBus> buses);

    TaskBus selectBus(LogicMessage message);

}
