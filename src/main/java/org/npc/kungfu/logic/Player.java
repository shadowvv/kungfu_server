package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.message.ApplyBattleRespMessage;
import org.npc.kungfu.logic.message.LoginRespMessage;
import org.npc.kungfu.logic.message.SSPlayerLogoutToMatch;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.IPassenger;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 玩家实体
 */
public class Player implements IPassenger<BaseMessage> {

    /**
     * 玩家id
     */
    private final int playerId;
    /**
     * 玩家昵称
     */
    private final String username;
    /**
     * 网络通道
     */
    private Channel channel;
    /**
     * 断线时间
     */
    private long channelInactiveTime;

    /**
     * 玩家角色id
     */
    private int roleId;
    /**
     * 匹配id
     */
    private int matchId;
    /**
     * 玩家所在战斗id
     */
    private int battleId;
    /**
     * 玩家是否在匹配
     */
    private boolean inMatch;
    /**
     * 玩家是否在战斗
     */
    private boolean inBattle;
    /**
     * 玩家接收的协议
     */
    private final ConcurrentLinkedQueue<BaseMessage> messages;

    /**
     * @param playerId 玩家id
     * @param userName 玩家昵称
     * @param channel  网络通道
     */
    Player(int playerId, String userName, Channel channel) {
        this.playerId = playerId;
        this.username = userName;
        this.channel = channel;
        this.messages = new ConcurrentLinkedQueue<>();
    }

    /**
     * 玩家选择武器，进入匹配
     * @param roleId     角色id
     */
    public void onPlayerApplyBattle(int roleId) {
        this.roleId = roleId;
        this.inMatch = true;
    }

    /**
     * 发送登录成功
     */
    public void sendLoginSuccess() {
        LoginRespMessage resp = new LoginRespMessage();
        resp.setPlayerId(playerId);
        resp.setSuccess(Boolean.TRUE);
        sendMessage(resp);
    }

    /**
     * 发送进去匹配成功
     */
    public void sendApplyBattleSuccess(int roleId, PlayerWeaponEnum weaponType) {
        ApplyBattleRespMessage resp = new ApplyBattleRespMessage();
        resp.setRoleId(roleId);
        resp.setWeaponType(weaponType.getTypeId());
        sendMessage(resp);
    }

    /**
     * 玩家掉线
     */
    public void onPlayerDisconnect() {
        if (channel.isActive()) {
            return;
        }
        this.channelInactiveTime = System.currentTimeMillis();
    }

    /**
     * 玩家重连
     *
     * @param channel 重连的网络通道
     */
    public void onPlayerReconnect(Channel channel) {
        if (!channel.isActive()) {
            return;
        }
        this.channelInactiveTime = 0;
        this.channel = channel;
    }

    /**
     * 退出匹配
     */
    public void exitMatch() {
        this.inMatch = false;
        this.channelInactiveTime = 0;
    }

    /**
     * 发送协议给玩家对应的客户端
     *
     * @param message 协议
     */
    public void sendMessage(BaseClientMessage message) {
        channel.writeAndFlush(message);
        System.out.println("send message playerId: " + this.playerId + " " + message.description());
    }

    @Override
    public boolean put(BaseMessage task) {
        this.messages.add(task);
        return true;
    }

    @Override
    public void doActions() {
        if (!messages.isEmpty()) {
            BaseMessage message = messages.poll();
            message.doAction(this);
        }
        heartbeat();
    }

    @Override
    public void heartbeat() {
        if (channelInactiveTime == 0) {
            return;
        }
        if (System.currentTimeMillis() - channelInactiveTime > 1000 * 20) {
            if (!messages.isEmpty()) {
                return;
            }
            if (inBattle) {
                return;
            }
            if (inMatch) {
                MessageDispatcher.getInstance().dispatchMessage(new SSPlayerLogoutToMatch(playerId, this.roleId), null);
            }
            PlayerService.getService().removePlayer(this);
        }
    }

    public int getRoleId() {
        return this.roleId;
    }

    public int getPlayerId() {
        return this.playerId;
    }

    public String getUserName() {
        return username;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public boolean isInBattle() {
        return inBattle;
    }

    public boolean isInMatch() {
        return inMatch;
    }

    public int getBattleId() {
        return battleId;
    }

    @Override
    public long getId() {
        return playerId;
    }

    @Override
    public String description() {
        return "playerId:" + playerId + " username:" + username + " inMatch:" + inMatch + " inBattle:" + inBattle;
    }
}
