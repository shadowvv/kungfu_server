package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.platfame.LogicMessage;

public interface ITaskStation extends Runnable {

    void putMessage(LogicMessage msg);
}
