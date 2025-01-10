package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.constant.GameStateEnum;
import org.npc.kungfu.logic.message.*;
import org.npc.kungfu.platfame.bus.IBus;
import org.npc.kungfu.platfame.math.GeometricAlgorithms;
import org.npc.kungfu.platfame.math.HitBox;
import org.npc.kungfu.platfame.math.Sector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.npc.kungfu.logic.constant.BattleConstants.WAIT_COMMAND_TICK;

/**
 * 决斗场
 */
public class BattleRing implements IBus<Role, BaseMessage> {

    /**
     * 战斗id
     */
    private final int battleId;
    /**
     * 消息队列
     */
    private final ArrayList<OperationReqMessage> messageList;
    /**
     * 参加决斗的角色
     */
    private final HashMap<Integer, Role> roles;
    /**
     * 决斗阶段
     */
    private GameStateEnum gameState = GameStateEnum.PREPARE;
    /**
     *
     */
    private long countDownTick = 0;

    private long lastTick = 0;

    /**
     * 决斗工厂
     *
     * @param roles 参与决斗的角色
     * @return 决斗场
     */
    public static BattleRing build(int battleId,List<Role> roles) {
        return new BattleRing(battleId,roles);
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
        messageList = new ArrayList<>();
        gameState = GameStateEnum.WAIT_COMMAND;
        countDownTick = WAIT_COMMAND_TICK;
    }

    /**
     * 接收消息
     *
     * @param message 玩家操作消息
     */
    public void onReceiveMessage(BaseMessage message) {
        if (message instanceof OperationReqMessage) {
            OperationReqMessage operationReqMessage = (OperationReqMessage) message;
            if (gameState == GameStateEnum.WAIT_COMMAND) {
                Player player = PlayerService.getService().getPlayer(operationReqMessage.getPlayerId());
                if (player != null) {
                    Role role = roles.get(player.getRole().getRoleId());
                    if (role != null) {
                        boolean operationSuccess = false;
                        if(role.onRoleMove(operationReqMessage.getX(), operationReqMessage.getY())){
                            if (role.onRoleHit(operationReqMessage.getFaceAngle())){
                                operationSuccess = true;
                            }
                        }
                        OperationRespMessage msg =  new OperationRespMessage();
                        msg.setSuccess(operationSuccess);
                        player.sendMessage(msg);
                    }
                }
                messageList.add(operationReqMessage);
            }
        }
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
            if (messageList.size() == roles.size() || countDownTick <= 0) {
                this.countDownTick = 0;
                gameState = GameStateEnum.ACTION;
                broadCastBattleResult();
            }
        }

//        if (gameState == GameStateEnum.ACTION) {
//            for (OperationReqMessage message : messageList) {
//                int roleId = message.getRoleId();
//                Role role = roles.get(roleId);
//                if (role != null) {
//                    message.doLogic(role);
//                }
//            }
//            messageList.clear();
//
//            checkHit();
//
//            for (Role role : roles.values()) {
//                if (role.getHpPoint() == 0) {
//                    gameState = GameStateEnum.END;
//                    break;
//                }
//            }
//            this.gameState = GameStateEnum.WAIT_COMMAND;
//            this.countDownTick = WAIT_COMMAND_TICK;
//            broadCastOperation();
//        }
//
//        if (gameState == GameStateEnum.END) {
//            broadBattleResult();
//        }
    }

    private void broadCastBattleResult() {
        BattleResultBroadMessage message = new BattleResultBroadMessage();
        List<RoleMessage> roleMessages = new LinkedList<>();
        for (OperationReqMessage req : messageList) {
            RoleMessage roleMessage = new RoleMessage();
            roleMessage.setRoleId(req.getRoleId());
            roleMessage.setX(req.getX());
            roleMessage.setY(req.getY());
            roleMessage.setFaceAngle(req.getFaceAngle());
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
    private boolean checkHit(Sector<Integer> attackSector, HitBox<Integer> hitBox) {
        return GeometricAlgorithms.isSectorCollidingWithRect(attackSector, hitBox);
    }

    @Override
    public long getId() {
        return battleId;
    }

    @Override
    public boolean put(Role passenger) {
        return false;
    }

    @Override
    public boolean putTask(long passengerId, BaseMessage Task) {
        return false;
    }

    @Override
    public Boolean arrived() {
        long currentTick = System.currentTimeMillis();
        if (this.lastTick == 0){
            this.lastTick = currentTick;
        }
        update(currentTick-this.countDownTick);
        this.lastTick = currentTick;
        return true;
    }

    @Override
    public boolean remove(long passengerId) {
        return false;
    }

    @Override
    public int getPassengerCount() {
        return 0;
    }

    @Override
    public String description() {
        return "";
    }

    @Override
    public List<Role> getPassengers() {
        return List.of();
    }
}
