package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.platfame.TaskStation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerService {

    private static final PlayerService service = new PlayerService();
    private PlayerService() {}

    public static PlayerService getService() {
        return service;
    }

    private ConcurrentHashMap<Integer, Player> players;
    private AtomicInteger playerId = new AtomicInteger(1);

    public void init(TaskStation playerStation) {
        players = new ConcurrentHashMap<>();
    }

    public void newPlayerLoginOver(Channel loginChannel) {
        int id = playerId.incrementAndGet();
        if (players.containsKey(id)) {
            return;
        }
        Player player = new Player(id,loginChannel);
        players.put(id, player);
        player.sendLoginSuccess();
    }

    public void onPlayerLoginOver(int playerId, Channel loginChannel) {
        if (players.containsKey(playerId)) {
            return;
        }
        Player player = new Player(playerId,loginChannel);
        players.put(playerId, player);
        player.sendLoginSuccess();
    }

    public void onPlayerLogout(int playerId) {
        Player player = players.remove(playerId);
        if (player != null) {
            player.onPlayerLoginOut();
        }
    }

    public void onPlayerApplyBattle(int playerId,int weaponType){
        Player player = players.get(playerId);
        if (player == null) {
            return;
        }
        player.onPlayerApplyBattle(weaponType);
    }
}
