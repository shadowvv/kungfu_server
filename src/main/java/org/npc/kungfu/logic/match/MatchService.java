package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.BusStation;
import org.npc.kungfu.platfame.bus.SoloPassengerBus;

/**
 * 匹配服务
 */
public class MatchService {

    private static final MatchService service = new MatchService();

    private MatchService() {
    }

    /**
     * @return 匹配服务单例
     */
    public static MatchService getService() {
        return service;
    }

    /**
     * 匹配业务调度器
     */
    private BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> taskStation;

    /**
     * 初始化
     * @param matchStation 匹配业务调度器
     */
    public void init(BusStation<SoloPassengerBus<MatchPool, BaseMessage>, MatchPool, BaseMessage> matchStation) {
        this.taskStation = matchStation;
    }

    /**
     * 投递消息
     * @param msg 消息
     */
    public void putMessage(BaseMessage msg) {
        taskStation.put(0L, msg);
    }
}
