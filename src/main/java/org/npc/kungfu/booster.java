package org.npc.kungfu;

import org.npc.kungfu.logic.MessageCoder;
import org.npc.kungfu.logic.MessageDispatcher;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchService;
import org.npc.kungfu.net.WebSocketServer;
import org.npc.kungfu.platfame.*;

public class booster {

    public static void main(String[] args) throws InterruptedException {

        TaskStation loginStation = new TaskStation(2);
        StationDriver loginDriver = new StationDriver(loginStation,0,30);
        loginDriver.runStation();

        TaskStation playerStation = new TaskStation(2);
        StationDriver playerDriver = new StationDriver(playerStation,0,30);
        playerDriver.runStation();

        TaskStation matchStation = new TaskStation(2);
        StationDriver matchDriver = new StationDriver(matchStation,0,30);
        matchDriver.runStation();

        TaskStation battleStation = new TaskStation(2);
        StationDriver battleDriver = new StationDriver(battleStation,0,30);
        battleDriver.runStation();

        LoginService loginService = new LoginService(loginStation);
        PlayerService playerService = new PlayerService(playerStation);
        MatchService matchService = new MatchService(matchStation);
        BattleService battleService = new BattleService(battleStation);

        WebSocketServer net = new WebSocketServer(8080,new MessageDispatcher(),new MessageCoder());
        net.start();
    }

}
