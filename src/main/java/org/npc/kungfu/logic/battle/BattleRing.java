package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.constant.GameStateEnum;
import org.npc.kungfu.logic.message.*;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.IPassenger;
import org.npc.kungfu.platfame.math2.CollisionUtils;
import org.npc.kungfu.platfame.math2.HitBox;
import org.npc.kungfu.platfame.math2.Sector;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.npc.kungfu.logic.constant.BattleConstants.WAIT_ACTION_TICK;
import static org.npc.kungfu.logic.constant.BattleConstants.WAIT_COMMAND_TICK;

/**
 * 决斗场
 */
public class BattleRing implements IPassenger<BaseMessage> {

    /**
     * 战斗id
     */
    private final int battleId;
    /**
     * 消息队列
     */
    private final HashMap<Integer, OperationReqMessage> messageHashMap;
    /**
     * 参加决斗的角色
     */
    private final HashMap<Integer, Role> roles;
    /**
     * 决斗阶段
     */
    private GameStateEnum gameState = GameStateEnum.PREPARE;
    /**
     * 倒计时
     */
    private long countDownTick = 0;
    /**
     * 上次tick
     */
    private long lastTick = 0;

    /**
     * 决斗工厂
     *
     * @param roles 参与决斗的角色
     * @return 决斗场
     */
    public static BattleRing build(int battleId, List<Role> roles) {
        return new BattleRing(battleId, roles);
    }

    /**
     * @param battleId 战斗id
     * @param roles    参与决斗的角色
     */
    private BattleRing(int battleId, List<Role> roles) {
        this.battleId = battleId;
        this.roles = new HashMap<>();
        for (Role role : roles) {
            this.roles.put(role.getRoleId(), role);
            role.bindBattleId(battleId);
        }
        messageHashMap = new HashMap<>();
    }

    public void start() {
        gameState = GameStateEnum.WAIT_COMMAND;
        countDownTick = WAIT_COMMAND_TICK;
        for (Role role : roles.values()) {
            role.sendMessage(new BattleStartPushMessage(gameState.getCode()));
        }
    }

    @Override
    public boolean put(BaseMessage task) {
        if (task instanceof OperationReqMessage) {
            OperationReqMessage operationReqMessage = (OperationReqMessage) task;
            if (gameState == GameStateEnum.WAIT_COMMAND) {
                Player player = PlayerService.getService().getPlayer(operationReqMessage.getPlayerId());
                if (player != null) {
                    Role role = roles.get(player.getRole().getRoleId());
                    if (role != null && !messageHashMap.containsKey(role.getRoleId())) {
                        operationReqMessage.setRole(role);
                        messageHashMap.put(role.getRoleId(), operationReqMessage);
                    }
                }
            }
        }
        return true;
    }

    public void onRoleLogout(Role role) {
        //TODO:结算战斗
        BattleService.getService().endBattle(this);
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

        //根据收到的玩家操作和倒计时切换战斗阶段
        if (gameState == GameStateEnum.WAIT_COMMAND) {
            this.countDownTick = this.countDownTick - deltaTime;
            if (messageHashMap.size() == roles.size() || this.countDownTick <= 0) {
                gameState = GameStateEnum.WAIT_ACTION;
                this.countDownTick = WAIT_ACTION_TICK;
                for (Role role : roles.values()) {
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

        if (gameState == GameStateEnum.WAIT_ACTION) {
            this.countDownTick = this.countDownTick - deltaTime;
            if (countDownTick <= 0) {
                gameState = GameStateEnum.ACTION;
                for (Role role : roles.values()) {
                    role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
                }
            }
            return;
        }

        if (gameState == GameStateEnum.ACTION) {
            checkHit();

            this.gameState = GameStateEnum.WAIT_COMMAND;
            this.countDownTick = WAIT_COMMAND_TICK;
            for (Role role : roles.values()) {
                role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
            }
            for (Role role : roles.values()) {
                if (role.getHpPoint() == 0) {
//                    gameState = GameStateEnum.END;
                    break;
                }
            }
            return;
        }

        if (gameState == GameStateEnum.END) {
            for (Role role : roles.values()) {
                role.sendMessage(new BattleStateBroadMessage(gameState.getCode()));
            }
            broadCastBattleResult();
            BattleService.getService().endBattle(this);
        }
    }

    private void broadCastBattleResult() {
        BattleResultBroadMessage message = new BattleResultBroadMessage();
        List<RoleMessage> roleMessages = new LinkedList<>();
        for (Role role : roles.values()) {
            RoleMessage roleMessage = new RoleMessage();
            roleMessage.setRoleId(role.getRoleId());
            roleMessage.setX((int) role.getCenter().getX());
            roleMessage.setY((int) role.getCenter().getY());
            roleMessage.setFaceAngle(role.getFaceAngle());
            roleMessages.add(roleMessage);
        }
        message.setRoleMessages(roleMessages);
        for (Role role : roles.values()) {
            role.sendMessage(message);
        }
    }

    /**
     * 检测角色之间是否击中
     */
    private void checkHit() {
        for (Role role : roles.values()) {
            for (Role otherRole : roles.values()) {
                if (role.getRoleId() != otherRole.getRoleId() && checkHit(role.getAttackSector(), otherRole.getHitBox())) {
                    otherRole.onRoleBeHit(role.getAttackPoint());
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

    @Override
    public long getId() {
        return battleId;
    }

    @Override
    public String description() {
        return "battle battleId: " + battleId;
    }
}
