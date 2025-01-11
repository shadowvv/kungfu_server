package org.npc.kungfu.platfame.bus;

public class SimpleBusStation<T extends ITask> extends BusStation<SoloPassengerBus<SoloPassenger<T>, T>, SoloPassenger<T>, T> {

    public SimpleBusStation(int threadNum, String busName) {
        super(threadNum, busName, new BusSequentialSelector<>());
        for (int i = 0; i < threadNum; i++) {
            SoloPassenger<T> soloPassenger = new SoloPassenger<>(i);
            SoloPassengerBus<SoloPassenger<T>, T> soloPassengerBus = new SoloPassengerBus<>(i, busName, soloPassenger);
            super.put(soloPassengerBus);
        }
    }

    @Override
    public boolean put(SoloPassengerBus<SoloPassenger<T>, T> bus) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public boolean put(SoloPassenger<T> passenger) {
        throw new UnsupportedOperationException("Not supported");
    }

}
