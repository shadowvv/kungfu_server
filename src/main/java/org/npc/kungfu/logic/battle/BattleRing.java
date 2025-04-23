package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.constant.BattleStateEnum;
import org.npc.kungfu.logic.message.*;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.math2.CollisionUtils;
import org.npc.kungfu.platfame.math2.HitBox;
import org.npc.kungfu.platfame.math2.Sector;

import java.util.*;

import static org.npc.kungfu.logic.constant.BattleConstants.*;

/**
 * 决斗场
 */
public class BattleRing implements IPassenger<BaseMessage> {

    /**
     * 决斗工厂
     *
     * @param roles 参与决斗的角色
     * @return 决斗场
     */
    public static BattleRing build(long battleId, List<BattleRole> roles) {
        return new BattleRing(battleId, roles);
    }

    /**
     * 战斗id
     */
    private final long battleId;
    /**
     * 参加决斗的角色
     */
    private final HashMap<Integer, BattleRole> roles;
    /**
     * 准备好的玩家人数
     */
    private int readyNum = 0;
    /**
     * 消息队列
     */
    private final HashMap<Integer, OperationReqMessage> messageHashMap;
    /**
     * 决斗阶段
     */
    private BattleStateEnum gameState = BattleStateEnum.PREPARE;
    /**
     * 倒计时
     */
    private long countDownTick = 0;
    /**
     * 上次tick
     */
    private long lastTick = 0;


    /**
     * @param battleId 战斗id
     * @param roles    参与决斗的角色
     */
    private BattleRing(long battleId, List<BattleRole> roles) {
        this.battleId = battleId;
        this.roles = new HashMap<>();
        for (BattleRole role : roles) {
            this.roles.put(role.getRoleId(), role);
            role.bindBattleId(battleId);
        }
        messageHashMap = new HashMap<>();
    }

    @Override
    public boolean put(BaseMessage task) {
        if (task instanceof OperationReqMessage) {
            OperationReqMessage operationReqMessage = (OperationReqMessage) task;
            if (gameState == BattleStateEnum.WAIT_COMMAND) {
                Player player = PlayerService.getService().getPlayer(operationReqMessage.getPlayerId());
                if (player != null) {
                    BattleRole role = roles.get(player.getRoleId());
                    if (role != null && !messageHashMap.containsKey(role.getRoleId())) {
                        operationReqMessage.setRole(role);
                        messageHashMap.put(role.getRoleId(), operationReqMessage);
                    }
                }
            }
        } else if (task instanceof LoadBattleReadyReqMessage) {
            LoadBattleReadyReqMessage loadBattleReadyReqMessage = (LoadBattleReadyReqMessage) task;
            readyNum++;
            if (readyNum == 2) {
                start();
            }
        }
        return true;
    }

    private void start() {
        gameState = BattleStateEnum.PREPARE;
        countDownTick = PREPARE_TICK;
        BattleStartBroadMessage message = buildBattleStartBroadMessage(roles.values());
        for (BattleRole role : roles.values()) {
            role.sendMessage(message);
        }
    }

    /**
     * 发送匹配结果
     *
     * @param list 匹配战斗的玩家
     * @return 匹配结果消息
     */
    private BattleStartBroadMessage buildBattleStartBroadMessage(Collection<BattleRole> list) {
        BattleStartBroadMessage battleStartBroadMessage = new BattleStartBroadMessage();
        List<RoleMessage> roleMessages = new ArrayList<>();
        for (BattleRole role : list) {
            RoleMessage roleMessage = new RoleMessage();
            roleMessage.setRoleId(role.getRoleId());
            roleMessage.setUserName(role.getRoleId() + "");
            roleMessage.setWeaponType(role.getWeaponType().getTypeId());
            roleMessage.setX(role.getCenter().getX());
            roleMessage.setY(role.getCenter().getY());
            roleMessage.setFaceAngle(role.getFaceAngle());
            roleMessage.setHp(5);
            roleMessages.add(roleMessage);
        }
        battleStartBroadMessage.setRoles(roleMessages);
        return battleStartBroadMessage;
    }

    @Override
    public void doActions() {
        heartbeat();
    }

    @Override
    public void heartbeat() {
        if (lastTick == 0) {
            lastTick = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        update(now - lastTick);
        lastTick = now;
    }

    /**
     * 更新决斗逻辑
     *
     * @param deltaTime 更新时间间隔
     */
    public void update(long deltaTime) {

        if (gameState == BattleStateEnum.PREPARE) {
            if (this.countDownTick > 0) {
                this.countDownTick = this.countDownTick - deltaTime;
                if (this.countDownTick <= 0) {
                    gameState = BattleStateEnum.WAIT_COMMAND;
                    this.countDownTick = WAIT_COMMAND_TICK;
                    for (BattleRole role : roles.values()) {
                        role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
                    }
                }
            }
            return;
        }

        //根据收到的玩家操作和倒计时切换战斗阶段
        if (gameState == BattleStateEnum.WAIT_COMMAND) {
            this.countDownTick = this.countDownTick - deltaTime;
            if (messageHashMap.size() == roles.size() || this.countDownTick <= 0) {
                gameState = BattleStateEnum.ACTION;
                for (BattleRole role : roles.values()) {
                    role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
                }

                for (OperationReqMessage req : messageHashMap.values()) {
                    req.doAction(this);
                }
                messageHashMap.clear();
                broadCastBattleResult();
            }
            return;
        }

        if (gameState == BattleStateEnum.ACTION) {
            checkHit();

            this.gameState = BattleStateEnum.WAIT_CLIENT_ACTION;
            this.countDownTick = WAIT_ACTION_TICK;
            for (BattleRole role : roles.values()) {
                role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
            }
            broadCastBattleResult();
            return;
        }

        if (gameState == BattleStateEnum.WAIT_CLIENT_ACTION) {
            this.countDownTick = this.countDownTick - deltaTime;
            if (countDownTick <= 0) {
                gameState = BattleStateEnum.WAIT_COMMAND;
                this.countDownTick = WAIT_COMMAND_TICK;
                for (BattleRole role : roles.values()) {
                    role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
                }
            }
            for (BattleRole role : roles.values()) {
                if (role.getHpPoint() == 0) {
                    gameState = BattleStateEnum.END;
                    System.out.println("战斗结束");
                    break;
                }
            }
            return;
        }

        if (gameState == BattleStateEnum.END) {
            for (BattleRole role : roles.values()) {
                role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
            }
            broadCastBattleResult();
            BattleService.getService().endBattle(this);
        }
    }

    private void broadCastBattleResult() {
        BattleResultBroadMessage message = new BattleResultBroadMessage();
        List<RoleMessage> roleMessages = new LinkedList<>();
        for (BattleRole role : roles.values()) {
            RoleMessage roleMessage = new RoleMessage();
            roleMessage.setRoleId(role.getRoleId());
            roleMessage.setX((int) role.getCenter().getX());
            roleMessage.setY((int) role.getCenter().getY());
            roleMessage.setFaceAngle(role.getFaceAngle());
            roleMessage.setHp(role.getHpPoint());
            roleMessages.add(roleMessage);
        }
        message.setRoleMessages(roleMessages);
        for (BattleRole role : roles.values()) {
            role.sendMessage(message);
        }
    }

    /**
     * 检测角色之间是否击中
     */
    private void checkHit() {
        for (BattleRole role : roles.values()) {
            for (BattleRole otherRole : roles.values()) {
                if (role.getRoleId() != otherRole.getRoleId() && checkHit(role.getAttackSector(), otherRole.getHitBox())) {
                    otherRole.onRoleBeHit(role.getAttackPoint());
                    System.out.println("击中了" + otherRole.getRoleId());
                }
            }
        }
    }

    /**
     * 检测是否击中
     *
     * @param attackSector 攻击范围
     * @param hitBox       受击盒
     * @return 是否击中
     */
    private boolean checkHit(Sector attackSector, HitBox hitBox) {
        return CollisionUtils.isSectorCollidingWithRect(attackSector, hitBox.getBoxVectors());
    }

    public void onRoleLogout(BattleRole role) {
        //TODO:结算战斗
        BattleService.getService().endBattle(this);
    }

    @Override
    public long getId() {
        return battleId;
    }

    @Override
    public String description() {
        return "battle battleId: " + battleId;
    }
}
