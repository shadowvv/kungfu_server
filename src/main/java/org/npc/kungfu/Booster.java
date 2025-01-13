package org.npc.kungfu;

import org.npc.kungfu.logic.*;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.match.MatchPoolSelector;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.net.WebSocketServer;
import org.npc.kungfu.platfame.bus.*;

public class Booster {

    public static void main(String[] args) throws InterruptedException {

        int threadNum = Runtime.getRuntime().availableProcessors() * 2;

        //初始化登录服务
        SimpleBusStation<BaseMessage> loginStation = new SimpleBusStation<>(threadNum, "login");
        StationDriver loginDriver = new StationDriver(loginStation, 100, 30);
        loginDriver.runStation();
        LoginService.getService().init(loginStation);

        //初始化玩家服务
        BusStation<Bus<Player, BaseMessage>, Player, BaseMessage> playerStation = new BusStation<>(threadNum, "player", new BusHashSelector<>());
        for (int i = 0; i < threadNum; i++) {
            playerStation.put(new Bus<>(i, "player"));
        }
        StationDriver playerDriver = new StationDriver(playerStation, 100, 30);
        playerDriver.runStation();
        PlayerService.getService().init(playerStation);

        //初始化匹配服务
        BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> matchStation = new BusStation<>(1, "match", new MatchPoolSelector());
        for (int i = 0; i < 1; i++) {
            MatchPool matchPool = new MatchPool(i, "match");
            matchStation.put(new SoloPassengerBus<>(i, "match", matchPool));
        }
        StationDriver matchDriver = new StationDriver(matchStation, 100, 30);
        matchDriver.runStation();
        MatchService.getService().init(matchStation);

        //初始化战斗服务
        BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> battleStation = new BusStation<>(threadNum, "battle", new BusHashSelector<>());
        StationDriver battleDriver = new StationDriver(battleStation, 100, 30);
        battleDriver.runStation();
        BattleService.getService().init(battleStation);

        //初始化网络服务
        WebSocketServer net = new WebSocketServer(8080, threadNum, MessageDispatcher.getInstance(), new MessageCoder());
        net.start();
    }

}
