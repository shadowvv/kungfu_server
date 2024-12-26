package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.ApplyBattleReqMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.TaskBus;
import org.npc.kungfu.platfame.TaskStation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerService {

    private static final PlayerService service = new PlayerService();

    private PlayerService() {
    }

    public static PlayerService getService() {
        return service;
    }

    private ConcurrentHashMap<Integer, Player> idPlayers;
    private ConcurrentHashMap<Channel, Player> channelPlayers;
    private AtomicInteger playerId = new AtomicInteger(1);
    private final TaskBus taskBus = new TaskBus();

    public void init(TaskStation playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        channelPlayers = new ConcurrentHashMap<>();
        playerStation.addBus(taskBus);
    }

    public void addMessage(BaseMessage msg, Channel senderChannel) {
        if (msg instanceof ApplyBattleReqMessage){
            ApplyBattleReqMessage req = (ApplyBattleReqMessage) msg;
            taskBus.addMessage(req);
        }
    }

    public void newPlayerLoginOver(Channel loginChannel) {
        int id = playerId.incrementAndGet();
        if (idPlayers.containsKey(id)) {
            return;
        }
        Player player = new Player(id, loginChannel);
        idPlayers.put(id, player);
        channelPlayers.put(loginChannel, player);
        player.sendLoginSuccess();
    }

    public void onPlayerLoginOver(int playerId, Channel loginChannel) {
        if (idPlayers.containsKey(playerId)) {
            return;
        }
        Player player = new Player(playerId, loginChannel);
        idPlayers.put(playerId, player);
        channelPlayers.put(loginChannel, player);
        player.sendLoginSuccess();
    }

    public void onPlayerLogout(int playerId) {
        Player player = idPlayers.remove(playerId);
        if (player != null) {
            channelPlayers.remove(player.getChannel());
            player.onPlayerLoginOut();
        }
    }

    public void onPlayerApplyBattle(int playerId, int weaponType) {
        Player player = idPlayers.get(playerId);
        if (player == null) {
            return;
        }
        player.onPlayerApplyBattle(weaponType);
    }

    public Player getPlayer(Channel senderChannel) {
        return channelPlayers.get(senderChannel);
    }
}
