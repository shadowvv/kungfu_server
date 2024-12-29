package org.npc.kungfu.logic;

import io.netty.channel.Channel;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.LoginReqMessage;
import org.npc.kungfu.platfame.bus.ITaskStation;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


public class LoginService {

    private static final LoginService service = new LoginService();

    public static LoginService getService() {
        return service;
    }

    public LoginService() {
    }

    private ITaskStation taskStation;

    private final AtomicInteger playerIdCreator = new AtomicInteger(0);
    private ConcurrentHashMap<Channel, Integer> channelPlayerIds;

    public void init(ITaskStation station) {
        taskStation = station;
        channelPlayerIds = new ConcurrentHashMap<>();
    }

    public void putMessage(BaseMessage msg, Channel senderChannel) {
        if (msg instanceof LoginReqMessage) {
            LoginReqMessage loginReqMsg = (LoginReqMessage) msg;
            loginReqMsg.setLoginChannel(senderChannel);
            taskStation.putMessage(msg);
        }
    }

    public Player createPlayer(Channel loginChannel) {
        int id = playerIdCreator.incrementAndGet();
        if (channelPlayerIds.containsKey(loginChannel)) {
            return null;
        }
        Player player = new Player(id, loginChannel);
        channelPlayerIds.put(loginChannel, player.getPlayerId());
        return player;
    }

    public Player LoadPlayer(int playerId, Channel loginChannel) {
        //TODO:
        return null;
    }

    public int getPlayerId(Channel senderChannel) {
        return channelPlayerIds.get(senderChannel);
    }
}
