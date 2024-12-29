package org.npc.kungfu.platfame.bus;

import org.npc.kungfu.net.LogicMessage;

public interface ITaskStation extends Runnable {

    void putMessage(LogicMessage msg);

    void putRunnable(Runnable r);
}
