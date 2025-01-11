package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.SoloPassenger;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LoginService {

    private static final LoginService service = new LoginService();

    private LoginService() {
    }

    public static LoginService getService() {
        return service;
    }

    private BusStation<SoloPassengerBus<SoloPassenger<BaseMessage>, BaseMessage>, SoloPassenger<BaseMessage>, BaseMessage> taskStation;
    private AtomicInteger playerIdCreator;
    private ConcurrentHashMap<String, Long> userNamePlayerIds;
    private ConcurrentHashMap<String, Boolean> userNameMutex;
    private ConcurrentHashMap<Channel, Boolean> channelMutex;

    public void init(BusStation<SoloPassengerBus<SoloPassenger<BaseMessage>, BaseMessage>, SoloPassenger<BaseMessage>, BaseMessage> station) {
        taskStation = station;
        playerIdCreator = new AtomicInteger(0);
        channelMutex = new ConcurrentHashMap<>();
        userNamePlayerIds = new ConcurrentHashMap<>();
        userNameMutex = new ConcurrentHashMap<>();
    }

    public void putMessage(BaseMessage msg) {
        taskStation.put(0, msg);
    }

    public Boolean enterUserNameMutex(String userName) {
        return userNameMutex.putIfAbsent(userName, true);
    }

    public void ExitMutex(String userName) {
        userNameMutex.remove(userName);
    }

    public Boolean enterChannelMutex(Channel senderChannel) {
        return channelMutex.putIfAbsent(senderChannel, true);
    }

    public void ExitMutex(Channel senderChannel) {
        channelMutex.remove(senderChannel);
    }

    public boolean checkUserName(String userName) {
        return !userNamePlayerIds.containsKey(userName);
    }

    public Player createPlayer(Channel loginChannel, String userName) {
        int id = playerIdCreator.incrementAndGet();
        Player player = new Player(id, userName, loginChannel);
        userNamePlayerIds.putIfAbsent(userName, player.getId());
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
}
