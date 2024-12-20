package org.npc.kungfu.logic;

import org.npc.kungfu.logic.constant.GameStateEnum;
import org.npc.kungfu.logic.message.OperationReqMessage;
import org.npc.kungfu.platfame.math.GeometricAlgorithms;
import org.npc.kungfu.platfame.math.HitBox;
import org.npc.kungfu.platfame.math.Sector;

import java.util.ArrayList;
import java.util.HashMap;

import static org.npc.kungfu.logic.constant.BattleConstants.BATTLE_RING_ROLE_NUM;
import static org.npc.kungfu.logic.constant.BattleConstants.WAIT_COMMAND_TICK;

/**
 * 决斗场
 */
public class BattleRing {

    private int battleId;
    /**
     * 消息队列
     */
    private ArrayList<OperationReqMessage> messageList;
    /**
     * 参加决斗的角色
     */
    private HashMap<Integer,Role> roles;
    /**
     * 决斗阶段
     */
    private GameStateEnum gameState = GameStateEnum.PREPARE;
    /**
     * 战斗结果
     */
    private BattleResult battleResult;
    /**
     *
     */
    private long countDownTick = 0;

    /**
     * 决斗工厂
     * @param roles 参与决斗的角色
     * @return 决斗场
     */
    public static BattleRing build(ArrayList<Role> roles) {
        return new BattleRing(roles);
    }

    /**
     *
     * @param roles 参与决斗的角色
     */
    private BattleRing(ArrayList<Role> roles) {
        this.roles = new HashMap<>();
        for (Role role : roles) {
            addRole(role);
        }
        messageList = new ArrayList<>();
        battleResult = new BattleResult();
    }

    /**
     * 添加决斗角色
     * @param role 战斗角色
     * @return 是否加入成功
     */
    private boolean addRole(Role role) {
        if (role == null) {
            return false;
        }

        if (roles.size() >= BATTLE_RING_ROLE_NUM){
            return false;
        }

        if (roles.containsKey(role.getRoleId())){
            return false;
        }

        roles.put(role.getRoleId(), role);
        if (roles.size() == BATTLE_RING_ROLE_NUM) {
            gameState = GameStateEnum.WAIT_COMMAND;
            countDownTick = WAIT_COMMAND_TICK;
        }
        return true;
    }

    /**
     * 接收消息
     * @param message 玩家操作消息
     */
    public void onReceiveMessage(OperationReqMessage message) {
        if (gameState == GameStateEnum.WAIT_COMMAND && messageList.size() < roles.size()) {
            messageList.add(message);
        }
    }

    /**
     * 更新决斗逻辑
     * @param deltaTime 更新时间间隔
     */
    public BattleResult update(long deltaTime) {

        //根据收到的玩家操作和倒计时切换战斗阶段
        if (gameState == GameStateEnum.WAIT_COMMAND) {
            this.countDownTick = this.countDownTick - deltaTime;
            if (messageList.size() == roles.size() || countDownTick <= 0) {
                this.countDownTick = 0;
                gameState = GameStateEnum.ACTION;
            }
        }

        if (gameState == GameStateEnum.ACTION) {
            for (OperationReqMessage message : messageList) {
                int roleId = message.getRoleId();
                Role role = roles.get(roleId);
                if (role != null) {
                    message.doLogic(role);
                }
            }
            messageList.clear();

            checkHit();

            for (Role role : roles.values()) {
                if (role.getHpPoint() == 0) {
                    gameState = GameStateEnum.END;
                    break;
                }
            }
            this.gameState = GameStateEnum.WAIT_COMMAND;
            this.countDownTick = WAIT_COMMAND_TICK;
        }

        if (gameState == GameStateEnum.END) {
            buildBattleResult();
        }
        return battleResult;
    }

    /**
     * 构建战斗结果
     */
    private void buildBattleResult() {
        for(Role role : roles.values()) {

        }
    }

    /**
     * 检测角色之间是否击中
     */
    private void checkHit(){
        for (Role role : roles.values()) {
            for (Role otherRole : roles.values()) {
                if (role.getRoleId() != otherRole.getRoleId() && checkHit(role.getAttackSector(),otherRole.getHitBox())){
                    otherRole.onRoleBeHit(role.getAttackPoint());
                }
            }
        }
    }

    /**
     * 检测是否击中
     * @param attackSector 攻击范围
     * @param hitBox 受击盒
     * @return 是否击中
     */
    private boolean checkHit(Sector<Integer> attackSector, HitBox<Integer> hitBox){
        return GeometricAlgorithms.isSectorCollidingWithRect(attackSector,hitBox);
    }

    public int getBattleId() {
        return battleId;
    }
}
