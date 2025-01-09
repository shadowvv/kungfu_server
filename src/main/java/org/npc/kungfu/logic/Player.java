package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.message.ApplyBattleRespMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginRespMessage;
import org.npc.kungfu.platfame.bus.IPassenger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Player implements IPassenger<BaseMessage> {

    private final int playerId;
    private final String username;
    private Channel channel;
    private long channelInactiveTime;

    private Role role;
    private boolean inMatch;
    private boolean inBattle;

    private final ConcurrentLinkedQueue<BaseMessage> messages;

    Player(int playerId, String userName, Channel channel) {
        this.playerId = playerId;
        this.username = userName;
        this.channel = channel;
        this.messages = new ConcurrentLinkedQueue<>();
    }

    public void sendLoginSuccess() {
        LoginRespMessage resp = new LoginRespMessage();
        resp.setPlayerId(playerId);
        resp.setSuccess(Boolean.TRUE);
        sendMessage(resp);
    }

    public void onPlayerApplyBattle(int weaponType) {
        if (role != null) {
            role.resetRole(weaponType);
        } else {
            role = Role.build(playerId, playerId, PlayerWeaponEnum.fromValue(weaponType), true, 1, 1, 10);
        }
        this.inMatch = true;
    }

    public void sendApplyBattleSuccess() {
        this.inMatch = true;

        ApplyBattleRespMessage resp = new ApplyBattleRespMessage();
        resp.setRoleId(role.getRoleId());
        resp.setWeaponType(this.role.getWeaponType());
        channel.writeAndFlush(resp);
    }

    @Override
    public boolean addTask(BaseMessage task) {
        this.messages.add(task);
        return true;
    }

    @Override
    public Boolean doActions() {
        if (!messages.isEmpty()) {
            BaseMessage message = messages.poll();
            message.doAction();
        }
        heartBeat();
        return true;
    }

    private void heartBeat() {
        if (channelInactiveTime == 0) {
            return;
        }
        if (System.currentTimeMillis() - channelInactiveTime > 1000 * 20) {
            if (!messages.isEmpty()) {
                return;
            }
            PlayerService.getService().removePlayer(this);
        }
    }

    public void onPlayerDisconnect() {
        if (channel.isActive()) {
            return;
        }
        this.channelInactiveTime = System.currentTimeMillis();
    }

    public void onPlayerReconnect(Channel channel) {
        if (!channel.isActive()) {
            return;
        }
        this.channelInactiveTime = 0;
        this.channel = channel;
    }

    public void sendMessage(BaseMessage message) {
        channel.writeAndFlush(message);
    }

    public Role getRole() {
        return role;
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

    @Override
    public long getId() {
        return playerId;
    }

    @Override
    public String description() {
        return "";
    }
}
