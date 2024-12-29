package org.npc.kungfu.net;

import com.google.gson.annotations.Expose;

public class LogicMessage {

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

    public void doLogic() {

    }
}
