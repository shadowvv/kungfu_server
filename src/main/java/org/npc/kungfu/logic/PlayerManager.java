package org.npc.kungfu.logic;

import java.util.HashMap;

public class PlayerManager {

    HashMap<Integer, Player> players;

    public PlayerManager() {

    }

    public void onPlayerLogin(int playerId) {
        if (players.containsKey(playerId)) {
            return;
        }
        Player player = new Player(playerId);
        players.put(playerId, player);
    }

    public void onPlayerReconnect(){

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
