package org.npc.kungfu.database;

public class GameInfoEntity {

    private int nextPlayerId;

    public GameInfoEntity() {

    }

    public void setNextPlayerId(int nextPlayerId) {
        this.nextPlayerId = nextPlayerId;
    }

    public int getNextPlayerId() {
        return nextPlayerId;
    }
}
