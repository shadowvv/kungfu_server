package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.ApplyBattleReqMessage;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;
import org.npc.kungfu.platfame.bus.TaskStation;

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
    private ConcurrentHashMap<Channel, Integer> channelPlayerIds;
    private final AtomicInteger playerId = new AtomicInteger(1);
    private ITaskStation taskStation;

    public void init(TaskStation playerStation) {
        idPlayers = new ConcurrentHashMap<>();
        channelPlayerIds = new ConcurrentHashMap<>();
        taskStation = playerStation;
    }

    public void putMessage(BaseMessage msg, Channel senderChannel) {
        if (msg instanceof ApplyBattleReqMessage){
            ApplyBattleReqMessage req = (ApplyBattleReqMessage) msg;
            req.setPlayerId(channelPlayerIds.get(senderChannel));
            taskStation.putMessage(req);
        }
    }

    public void newPlayerLoginOver(Channel loginChannel) {
        int id = playerId.incrementAndGet();
        if (idPlayers.containsKey(id)) {
            return;
        }
        Player player = new Player(id, loginChannel);
        idPlayers.put(id, player);
        channelPlayerIds.put(loginChannel, player.getPlayerId());
        player.sendLoginSuccess();
    }

    public void onPlayerLoginOver(int playerId, Channel loginChannel) {
        if (idPlayers.containsKey(playerId)) {
            return;
        }
        Player player = new Player(playerId, loginChannel);
        idPlayers.put(playerId, player);
        channelPlayerIds.put(loginChannel, player.getPlayerId());
        player.sendLoginSuccess();
    }

    public void onPlayerLogout(int playerId) {
        Player player = idPlayers.remove(playerId);
        if (player != null) {
            channelPlayerIds.remove(player.getChannel());
            player.onPlayerLoginOut();
        }
    }

    public void onPlayerApplyBattle(int playerId, int weaponType) {
        Player player = idPlayers.get(playerId);
        if (player == null) {
            return;
        }
        if (player.isInBattle()){
            return;
        }
        if (player.isInMatch()){
            return;
        }
        player.onPlayerApplyBattle(weaponType);
        MatchService.getService().enterMatch(player.getRole());
        System.out.println("enter match");
    }

    public Player getPlayer(Channel senderChannel) {
        if (!channelPlayerIds.containsKey(senderChannel)) {
            return null;
        }
        return idPlayers.get(channelPlayerIds.get(senderChannel));
    }
}
