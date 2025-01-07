package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.message.ApplyBattleRespMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginRespMessage;
import org.npc.kungfu.platfame.bus.IFixedPassenger;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Player implements IFixedPassenger<BaseMessage> {

    private final int playerId;
    private final String username;
    private final Channel channel;

    private Role role;
    private boolean inMatch;
    private boolean inBattle;

    private final ConcurrentLinkedQueue<BaseMessage> luggageQueue;

    Player(int playerId, String userName, Channel channel) {
        this.playerId = playerId;
        this.username = userName;
        this.channel = channel;
        this.luggageQueue = new ConcurrentLinkedQueue<>();
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

    public boolean isInBattle() {
        return inBattle;
    }

    public boolean isInMatch() {
        return inMatch;
    }

    public Role getRole() {
        return role;
    }

    public void sendMessage(BaseMessage message) {
        channel.writeAndFlush(message);
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getUserName() {
        return username;
    }

    @Override
    public boolean putLuggage(BaseMessage luggage) {
        this.luggageQueue.add(luggage);
        return true;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public void doLogic() {
        if (!luggageQueue.isEmpty()) {
            BaseMessage message = luggageQueue.poll();
            message.doLogic();
        }
    }

    @Override
    public String getDescription() {
        return "";
    }
}
