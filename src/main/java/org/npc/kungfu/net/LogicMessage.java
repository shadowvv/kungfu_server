package org.npc.kungfu.net;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.platfame.bus.ITask;

/**
 * 业务消息
 */
public abstract class LogicMessage implements ITask {

    /**
     * 消息id
     */
    @Expose
    private int id;

    /**
     * @param id 消息id
     */
    public LogicMessage(int id) {
        this.id = id;
    }

    /**
     * @return 消息id
     */
    public int getId() {
        return id;
    }
}
