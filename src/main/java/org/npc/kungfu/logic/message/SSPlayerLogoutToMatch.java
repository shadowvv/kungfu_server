package org.npc.kungfu.logic.message;

import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.message.base.BaseServerMatchMessage;

public class SSPlayerLogoutToMatch extends BaseServerMatchMessage {

    /**
     * 玩家id
     */
    private final int playerId;
    /**
     * 角色id
     */
    private final long roleId;

    /**
     * @param playerId 玩家id
     * @param roleId   角色id
     */
    public SSPlayerLogoutToMatch(int playerId, long roleId) {
        super(20004);
        this.playerId = playerId;
        this.roleId = roleId;
    }

    public long getRoleId() {
        return roleId;
    }

    @Override
    public void doAction(MatchPool pool) {

    }

    @Override
    public String description() {
        return "SSPlayerLogoutToMatch playerId: " + playerId + " roleId: " + roleId;
    }
}
