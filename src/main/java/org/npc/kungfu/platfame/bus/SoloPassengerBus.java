package org.npc.kungfu.platfame.bus;

import java.util.List;

/**
 * 单处理器业务组
 *
 * @param <T> 业务处理器
 * @param <V> 具体业务
 */
public class SoloPassengerBus<T extends SimplePassenger<V>, V extends ITask> implements IBus<T, V> {

    /**
     * 业务组
     */
    private final Bus<T, V> bus;

    /**
     * @param id            业务组id
     * @param busName       业务组名
     * @param soloPassenger 单处理器
     */
    public SoloPassengerBus(long id, String busName, T soloPassenger) {
        this.bus = new Bus<>(id, busName);
        this.bus.put(soloPassenger);
    }

    /**
     * 屏蔽添加处理器方法
     *
     * @param passenger 业务处理器
     * @return 不支持
     */
    @Override
    public boolean put(T passenger) {
        throw new UnsupportedOperationException("This method is unsupported");
    }

    @Override
    public boolean put(long passengerId, V Task) {
        return bus.put(passengerId, Task);
    }

    @Override
    public boolean arrived() {
        return bus.arrived();
    }

    @Override
    public List<T> getPassengers() {
        return this.bus.getPassengers();
    }

    /**
     * 屏蔽移除处理器方法
     *
     * @param passengerId 业务处理器id
     * @return 不支持
     */
    @Override
    public boolean remove(long passengerId) {
        throw new UnsupportedOperationException("This method is unsupported");
    }

    @Override
    public int getPassengerCount() {
        return bus.getPassengerCount();
    }

    @Override
    public long getId() {
        return bus.getId();
    }

    @Override
    public String description() {
        return "SoloPassengerBus:" + bus.description();
    }
}
