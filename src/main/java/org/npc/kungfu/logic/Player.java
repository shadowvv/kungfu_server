package org.npc.kungfu.logic;

import com.google.gson.Gson;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.LoginRespMessage;

public class Player {

    private final int playerId;
    private Channel channel;
    private Role role;

    Player(int playerId, Channel channel) {
        this.playerId = playerId;
        this.channel = channel;
    }

    public void onPlayerApplyBattle(int weaponType) {
        if (role != null) {
            role.resetRole(weaponType);
        }
        role = Role.build(1, playerId, PlayerWeaponEnum.fromValue(weaponType), true, 1, 1, 10);
        MatchService.enterMatch(role);
    }

    public void onBattleOver() {

    }

    public int getPlayerId() {
        return playerId;
    }

    public void onPlayerLoginOut() {
    }

    public void sendLoginSuccess() {
        Gson gson = new Gson();
        String message = gson.toJson(new LoginRespMessage(true));
        System.out.println(message);
        channel.writeAndFlush(new TextWebSocketFrame(message));
    }

    public Channel getChannel() {
        return channel;
    }
}
