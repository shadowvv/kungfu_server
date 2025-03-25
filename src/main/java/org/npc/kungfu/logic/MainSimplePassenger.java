package org.npc.kungfu.logic;

import org.npc.kungfu.platfame.bus.ITask;
import org.npc.kungfu.platfame.bus.SimplePassenger;

public class MainSimplePassenger<T extends ITask> extends SimplePassenger<T> {

    private long lastTick = 0;

    /**
     * @param id 业务id
     */
    public MainSimplePassenger(long id) {
        super(id);
    }

    @Override
    public void heartbeat() {
        if (System.currentTimeMillis() - lastTick > 1000 * 10) {
            lastTick = System.currentTimeMillis();
            LoginService.getService().updateNextPlayerIdToDB();
        }
    }
}
