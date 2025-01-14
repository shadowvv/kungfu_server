package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BattleService {

    private static final BattleService service = new BattleService();

    public static BattleService getService() {
        return service;
    }

    private BattleService() {

    }

    private BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> taskStation;
    private HashMap<Long, BattleRing> battleRingHashMap;
    private AtomicInteger battleIdCounter;

    public void init(BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> battleStation) {
        taskStation = battleStation;
        battleRingHashMap = new HashMap<>();
        battleIdCounter = new AtomicInteger(0);
    }

    public void startBattle(List<Role> roles) {
        BattleRing battleRing = BattleRing.build(battleIdCounter.incrementAndGet(), roles);
        battleRingHashMap.put(battleRing.getId(), battleRing);
        taskStation.put(battleRing);
        battleRing.start();
    }

    public void stopBattle() {

    }

    public boolean endBattle(BattleRing battleRing) {
        return taskStation.remove(battleRing);
    }

    public void putMessage(BaseClientMessage msg) {
        Player player = PlayerService.getService().getPlayer(msg.getPlayerId());
        if (player == null) {
            return;
        }
        int battleId = player.getRole().getBattleId();
        taskStation.put(battleId, msg);
    }

    public void putMessage(BaseServerMessage serverMessage) {
    }
}
