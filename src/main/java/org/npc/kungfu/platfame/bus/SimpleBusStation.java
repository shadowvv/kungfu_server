package org.npc.kungfu.platfame.bus;

/**
 * 简单业务调度器
 *
 * @param <T> 业务
 */
public class SimpleBusStation<T extends ITask> extends BusStation<SoloPassengerBus<SimplePassenger<T>, T>, SimplePassenger<T>, T> {

    public SimpleBusStation(int threadNum, String busName) {
        super(threadNum, busName, new BusSequentialSelector<>());
        for (int i = 0; i < threadNum; i++) {
            SimplePassenger<T> soloPassenger = new SimplePassenger<>(i);
            SoloPassengerBus<SimplePassenger<T>, T> soloPassengerBus = new SoloPassengerBus<>(i, busName, soloPassenger);
            super.put(soloPassengerBus);
        }
    }

    /**
     * 屏蔽添加业务组功能
     *
     * @param bus 业务组
     * @return 不支持
     */
    @Override
    public boolean put(SoloPassengerBus<SimplePassenger<T>, T> bus) {
        throw new UnsupportedOperationException("Not supported");
    }

    /**
     * 屏蔽添加业务处理器功能
     *
     * @param passenger 业务处理器
     * @return 不支持
     */
    @Override
    public boolean put(SimplePassenger<T> passenger) {
        throw new UnsupportedOperationException("Not supported");
    }

}
