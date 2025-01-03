package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.platfame.bus.IBusSelector;

import java.util.List;

public class MatchPoolSelector implements IBusSelector<Role,MatchPool> {

    private List<MatchPool> pools;

    @Override
    public void init(List<MatchPool> buses) {
        this.pools = buses;
    }

    @Override
    public MatchPool selectBus(Role passenger) {
        return this.pools.get(0);
    }
}
