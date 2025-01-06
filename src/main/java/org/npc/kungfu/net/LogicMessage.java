package org.npc.kungfu.net;

import com.google.gson.annotations.Expose;
import org.npc.kungfu.platfame.bus.IPassenger;

public abstract class LogicMessage implements IPassenger {

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

    public abstract void doLogic();
}
