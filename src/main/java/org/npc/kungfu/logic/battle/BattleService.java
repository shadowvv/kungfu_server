package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.match.MatchRole;
import org.npc.kungfu.logic.message.base.BaseClientMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.logic.message.base.BaseServerMessage;
import org.npc.kungfu.platfame.bus.Bus;
import org.npc.kungfu.platfame.bus.BusStation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 战斗服务
 */
public class BattleService {

    private static final BattleService service = new BattleService();

    private BattleService() {

    }

    /**
     * @return 战斗服务单例
     */
    public static BattleService getService() {
        return service;
    }

    /**
     * 战斗业务调度器
     */
    private BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> taskStation;
    /**
     * 战斗id和对应战斗的映射
     */
    private HashMap<Long, BattleRing> battleRingHashMap;
    /**
     * 战斗id生成器
     */
    private AtomicLong battleIdCounter;

    /**
     * 初始化
     * @param battleStation 战斗调度器
     */
    public void init(BusStation<Bus<BattleRing, BaseMessage>, BattleRing, BaseMessage> battleStation) {
        taskStation = battleStation;
        battleRingHashMap = new HashMap<>();
        battleIdCounter = new AtomicLong(0);
    }

    /**
     * 开始战斗
     * @param roles 战斗角色
     */
    public void startBattle(List<MatchRole> roles) {
        List<BattleRole> battleRoles = new ArrayList<>();
        for (MatchRole role : roles) {
            BattleRole battleRole = BattleRole.build(role.getRoleId(), role.getPlayerId(), role.getWeaponType(), role.getCenter().getX(), role.getCenter().getY(), role.getFaceAngle());
            battleRoles.add(battleRole);
        }
        BattleRing battleRing = BattleRing.build(battleIdCounter.incrementAndGet(), battleRoles);
        battleRingHashMap.put(battleRing.getId(), battleRing);
        taskStation.put(battleRing);

        for (BattleRole battleRole : battleRoles) {
            Player player = PlayerService.getService().getPlayer(battleRole.getPlayerId());
            if (player != null) {
                player.setBattleId(battleRing.getId());
            }
        }
    }

    /**
     *
     */
    public void stopBattle() {

    }

    /**
     * 结束战斗
     * @param battleRing 战斗
     * @return 是否结束
     */
    public boolean endBattle(BattleRing battleRing) {
        return taskStation.remove(battleRing);
    }

    /**
     * 投递消息
     * @param msg 客户端消息
     */
    public void putMessage(BaseClientMessage msg) {
        Player player = PlayerService.getService().getPlayer(msg.getPlayerId());
        if (player == null) {
            return;
        }
        long battleId = player.getBattleId();
        taskStation.put(battleId, msg);
    }

    /**
     * 投递服务器消息
     * @param serverMessage 服务器消息
     */
    public void putMessage(BaseServerMessage serverMessage) {
    }
}
