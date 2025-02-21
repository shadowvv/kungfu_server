package org.npc.kungfu.logic;

import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 角色基类
 */
public abstract class BaseRole {

    /**
     * 角色id
     */
    private final int roleId;
    /**
     * 玩家id
     */
    private final int playerId;

    /**
     * @param roleId   角色id
     * @param playerId 玩家id
     */
    public BaseRole(int roleId, int playerId) {
        this.roleId = roleId;
        this.playerId = playerId;
    }

    /**
     * 获取角色id
     *
     * @return 角色id
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * 获取玩家id
     *
     * @return 玩家id
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * 发送消息
     *
     * @param message 消息
     */
    public void sendMessage(BaseClientMessage message) {
        PlayerService playerService = PlayerService.getService();
        if (playerService == null) {
            Logger logger = LoggerFactory.getLogger(BaseRole.class);
            logger.error("PlayerService is null");
            return;
        }

        Player player = playerService.getPlayer(this.playerId);
        if (player != null) {
            player.sendMessage(message);
            Logger logger = LoggerFactory.getLogger(BaseRole.class);
            logger.info("send message playerId: {} {}", this.playerId, message.description());
        }
    }
}
