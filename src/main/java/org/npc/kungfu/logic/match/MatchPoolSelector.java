package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.platfame.bus.IBusSelector;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;

import java.util.List;

public class MatchPoolSelector implements IBusSelector<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> {

    List<SoloPassengerBus<MatchPool, BaseMessage>> pools;

    @Override
    public void init(List<SoloPassengerBus<MatchPool, BaseMessage>> buses) {
        this.pools = buses;
    }

    @Override
    public SoloPassengerBus<MatchPool, BaseMessage> selectBus(MatchPool passenger) {
        //TODO:
        return this.pools.get(0);
    }
}
