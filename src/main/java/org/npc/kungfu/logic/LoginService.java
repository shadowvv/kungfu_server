package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LoginService {

    private static final LoginService service = new LoginService();

    private LoginService() {
    }

    public static LoginService getService() {
        return service;
    }

    private BusStation<BaseMessage, Bus<BaseMessage>> taskStation;
    private AtomicInteger playerIdCreator;
    private ConcurrentHashMap<Channel, Integer> channelPlayerIds;
    private ConcurrentHashMap<String, Integer> userNamePlayerIds;
    private ConcurrentHashMap<String, Boolean> userNameMutex;

    public void init(BusStation<BaseMessage,Bus<BaseMessage>> station) {
        taskStation = station;
        playerIdCreator = new AtomicInteger(0);
        channelPlayerIds = new ConcurrentHashMap<>();
        userNamePlayerIds = new ConcurrentHashMap<>();
        userNameMutex = new ConcurrentHashMap<>();
    }

    public void putMessage(BaseMessage msg) {
        taskStation.put(msg);
    }

    public Boolean enterMutex(String userName) {
        return userNameMutex.putIfAbsent(userName, true);
    }

    public void ExitMutex(String userName) {
        userNameMutex.remove(userName);
    }

    public boolean checkUserName(String userName) {
        return !userNamePlayerIds.containsKey(userName);
    }

    public Player createPlayer(Channel loginChannel, String userName) {
        if (channelPlayerIds.containsKey(loginChannel)) {
            return null;
        }
        int id = playerIdCreator.incrementAndGet();
        Player player = new Player(id, userName, loginChannel);
        channelPlayerIds.putIfAbsent(loginChannel, player.getPlayerId());
        userNamePlayerIds.putIfAbsent(userName, player.getPlayerId());
        return player;
    }

    //TODO:
    public void onPlayerLoginSuccess(Player player) {
        PlayerService.getService().onPlayerLoginSuccess(player);
    }

    //TODO:
    public Player LoadPlayer(int playerId, Channel loginChannel) {
        return null;
    }

    //TODO:
    public void onChannelInactive(Channel channel) {
    }

    public int getPlayerId(Channel senderChannel) {
        return channelPlayerIds.get(senderChannel);
    }
}
