package org.npc.kungfu.net;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.platfame.bus.ITask;

public abstract class LogicMessage implements ITask {

    @Expose
    private int id;

    public LogicMessage() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
