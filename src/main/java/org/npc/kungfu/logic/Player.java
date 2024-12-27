package org.npc.kungfu.logic;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.message.LoginRespMessage;

public class Player {

    private final int playerId;
    private Channel channel;
    private Role role;
    private boolean inMatch;
    private boolean inBattle;

    Player(int playerId, Channel channel) {
        this.playerId = playerId;
        this.channel = channel;
    }

    public void onPlayerApplyBattle(int weaponType) {
        if (role != null) {
            role.resetRole(weaponType);
        }else {
            role = Role.build(1, playerId, PlayerWeaponEnum.fromValue(weaponType), true, 1, 1, 10);
        }
        this.inMatch = true;
    }

    public void onBattleOver() {
        this.inBattle = false;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void onPlayerLoginOut() {
    }

    public void sendLoginSuccess() {
        LoginRespMessage resp = new LoginRespMessage();
        resp.setPlayerId(playerId);
        resp.setSuccess(Boolean.TRUE);

        Gson gson = new Gson();
        String message = gson.toJson(resp);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public Channel getChannel() {
        return channel;
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
}
