package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.IBusSelector;
import org.npc.kungfu.platfame.bus.IBusStation;

import java.util.ArrayList;
import java.util.List;

public class MatchBusStation implements IBusStation<Role> {

    private final BusStation<Role,MatchPool> busStation;

    public MatchBusStation(int busCount, String busName, IBusSelector<Role, MatchPool> selector){
        List<MatchPool> list = new ArrayList<>();
        for (int i = 0; i < busCount; i++) {
            list.add(new MatchPool(busName));
        }
        busStation = new BusStation<>(busCount,busName,list,selector);
    }

    @Override
    public void put(Role passenger) {
        busStation.put(passenger);
    }

    @Override
    public void run() {
        busStation.run();
    }
}
