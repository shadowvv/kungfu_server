package org.npc.kungfu;

import org.npc.kungfu.logic.LoginService;
import org.npc.kungfu.logic.MessageCoder;
import org.npc.kungfu.logic.MessageDispatcher;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchPool;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.net.WebSocketServer;
import org.npc.kungfu.platfame.bus.BusSequentialSelector;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.StationDriver;

public class Booster {

    public static void main(String[] args) throws InterruptedException {

        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        //初始化登录服务
        BusStation<BaseMessage> loginStation = new BusStation<>(threadNum, "login", new BusSequentialSelector<>());
        StationDriver loginDriver = new StationDriver(loginStation, 100, 30);
        loginDriver.runStation();
        LoginService.getService().init(loginStation);

        //初始化玩家服务
        BusStation<BaseMessage> playerStation = new BusStation<>(threadNum, "player", new BusSequentialSelector<>());
        StationDriver playerDriver = new StationDriver(playerStation, 100, 30);
        playerDriver.runStation();
        PlayerService.getService().init(playerStation);

        //初始化匹配服务
        BusStation<MatchPool> matchStation = new BusStation<>(1, "match", new BusSequentialSelector<>());
        StationDriver matchDriver = new StationDriver(matchStation, 100, 30);
        matchDriver.runStation();
        MatchService.getService().init(matchStation);

        //初始化战斗服务
        BusStation<BattleRing> battleStation = new BusStation<>(threadNum, "battle", new BusSequentialSelector<>());
        StationDriver battleDriver = new StationDriver(battleStation, 100, 30);
        battleDriver.runStation();
        BattleService.getService().init(battleStation);

        //初始化网络服务
        WebSocketServer net = new WebSocketServer(8080, threadNum, new MessageDispatcher(), new MessageCoder());
        net.start();
    }

}
