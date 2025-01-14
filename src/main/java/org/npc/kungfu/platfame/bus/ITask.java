package org.npc.kungfu.platfame.bus;

/**
 * 具体业务
 */
public interface ITask {

    /**
     * 执行业务
     *
     * @param passenger 业务处理器
     */
    void doAction(IPassenger<? extends ITask> passenger);

    /**
     * @return 业务描述
     */
    String description();

}
