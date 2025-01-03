package org.npc.kungfu;

import org.npc.kungfu.logic.*;
import org.npc.kungfu.logic.battle.BattleRing;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.match.MatchBusStation;
import org.npc.kungfu.logic.match.MatchPoolSelector;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.net.WebSocketServer;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusSequentialSelector;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.StationDriver;

import java.util.ArrayList;
import java.util.List;

public class Booster {

    public static void main(String[] args) throws InterruptedException {

        int threadNum = Runtime.getRuntime().availableProcessors() * 2;
        //初始化登录服务
        List<Bus<BaseMessage>> buses = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            buses.add(new Bus<>("login"));
        }
        BusStation<BaseMessage, Bus<BaseMessage>> loginStation = new BusStation<>(threadNum, "login", buses,new BusSequentialSelector<>());
        StationDriver loginDriver = new StationDriver(loginStation, 100, 30);
        loginDriver.runStation();
        LoginService.getService().init(loginStation);

        //初始化玩家服务
        List<Bus<BaseMessage>> buses2 = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            buses2.add(new Bus<>("player"));
        }
        BusStation<BaseMessage,Bus<BaseMessage>> playerStation = new BusStation<>(threadNum, "player", buses2,new BusSequentialSelector<>());
        StationDriver playerDriver = new StationDriver(playerStation, 100, 30);
        playerDriver.runStation();
        PlayerService.getService().init(playerStation);

        //初始化匹配服务
        MatchBusStation matchStation = new MatchBusStation(1, "match", new MatchPoolSelector());
        StationDriver matchDriver = new StationDriver(matchStation, 100, 30);
        matchDriver.runStation();
        MatchService.getService().init(matchStation);

        //初始化战斗服务
        List<Bus<BattleRing>> battleBuses = new ArrayList<>();
        for (int i = 0; i < threadNum; i++) {
            battleBuses.add(new Bus<>("battle"));
        }
        BusStation<BattleRing,Bus<BattleRing>> battleStation = new BusStation<>(threadNum, "battle", battleBuses,new BusSequentialSelector<>());
        StationDriver battleDriver = new StationDriver(battleStation, 100, 30);
        battleDriver.runStation();
        BattleService.getService().init(battleStation);

        //初始化网络服务
        WebSocketServer net = new WebSocketServer(8080, threadNum, new MessageDispatcher(), new MessageCoder());
        net.start();
    }

}
