package org.npc.kungfu.logic;

import org.npc.kungfu.logic.constant.PlayerActionTypeEnum;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.message.BaseMessage;
import org.npc.kungfu.logic.message.MatchResultBroadMessage;
import org.npc.kungfu.net.LogicMessage;
import org.npc.kungfu.platfame.math.HitBox;
import org.npc.kungfu.platfame.math.Sector;
import org.npc.kungfu.platfame.math.VectorTwo;

import static org.npc.kungfu.logic.constant.BattleConstants.HIT_BOX_HEIGHT;
import static org.npc.kungfu.logic.constant.BattleConstants.HIT_BOX_WIDTH;

/**
 * 玩家操作的角色
 */
public class Role {

    /**
     * 角色id
     */
    private final int roleId;
    /**
     * 玩家id
     */
    private final int playerId;
    /**
     * 角色所在战斗id
     */
    private int battleId;
    /**
     * 角色是否为激活状态
     */
    private boolean active;

    /**
     * 角色使用的武器
     */
    private PlayerWeaponEnum weaponType;
    /**
     * 角色当前行为
     */
    private PlayerActionTypeEnum actionType;
    /**
     * 角色攻击力
     */
    private final int attackPoint;
    /**
     * 角色移动范围
     */
    private final int moveRange;
    /**
     * 角色生命值
     */
    private int hpPoint;

    /**
     * 角色中心点
     */
    private VectorTwo<Integer> center;
    /**
     * 角色受击盒
     */
    private HitBox<Integer> hitBox;
    /**
     * 角色攻击范围
     */
    private Sector<Integer> attackSector;
    /**
     * 角色朝向
     */
    private int faceAngle;

    /**
     * 角色工厂
     *
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器
     * @param active     是否已激活
     * @param positionX  位置x坐标
     * @param positionY  位置y坐标
     * @param faceAngle  朝向
     * @return 战斗角色
     */
    public static Role build(int roleId, int playerId, PlayerWeaponEnum weaponType, boolean active, int positionX, int positionY, int faceAngle) {
        return new Role(roleId, playerId, weaponType, active, positionX, positionY, faceAngle);
    }

    /**
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器
     * @param active     是否已激活
     * @param positionX  位置x坐标
     * @param positionY  位置y坐标
     * @param faceAngle  朝向
     */
    private Role(int roleId, int playerId, PlayerWeaponEnum weaponType, boolean active, int positionX, int positionY, int faceAngle) {
        this.roleId = roleId;
        this.playerId = playerId;
        this.weaponType = weaponType;
        this.active = active;
        this.actionType = PlayerActionTypeEnum.MOVE;
        this.center = VectorTwo.createIntegerVector(positionX, positionY);
        this.faceAngle = faceAngle;

        this.attackPoint = this.weaponType.getAttack();
        this.moveRange = this.weaponType.getMoveRange();
        this.hitBox = HitBox.createIntegerHitBox(positionX, positionY, HIT_BOX_WIDTH, HIT_BOX_HEIGHT);
        this.attackSector = Sector.createIntegerSector(positionX, positionY, this.weaponType.getAttackInnerRadius(), this.weaponType.getAttackOuterRadius(), this.weaponType.getStartAngle(), this.weaponType.getEndAngle());
    }

    public void resetRole(int weaponType) {
    }

    /**
     * 角色移动
     *
     * @param x 目标x坐标
     * @param y 目标y坐标
     * @return 移动是否成功
     */
    public boolean onRoleMove(int x, int y) {
        if (!this.active) {
            return false;
        }

        if (this.actionType != PlayerActionTypeEnum.MOVE) {
            return false;
        }

        if (this.center.inCirCle(x, y, moveRange)) {
            return false;
        }

        this.center.moveTo(x, y);
        this.actionType = PlayerActionTypeEnum.ATTACK;
        return true;
    }

    /**
     * 角色转向
     *
     * @param faceAngle 角色朝向
     * @return 转向是否成功
     */
    public boolean onRoleHit(int faceAngle) {
        if (!this.active) {
            return false;
        }
        if (this.actionType != PlayerActionTypeEnum.ATTACK) {
            return false;
        }
        this.faceAngle = faceAngle;
        this.actionType = PlayerActionTypeEnum.MOVE;
        return true;
    }

    /**
     * 角色被击中
     *
     * @param attackPoint 攻击力
     * @return 剩余血量
     */
    public int onRoleBeHit(int attackPoint) {
        this.hpPoint = this.hpPoint - attackPoint;
        if (this.hpPoint < 0) {
            this.hpPoint = 0;
        }
        return this.hpPoint;
    }

    /**
     * 设置玩家是否激活
     *
     * @param active 激活
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * @return 玩家是否激活
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @return 攻击力
     */
    public int getAttackPoint() {
        return this.attackPoint;
    }

    /**
     * @return 生命值
     */
    public int getHpPoint() {
        return hpPoint;
    }

    /**
     * @return 角色受击盒
     */
    public HitBox<Integer> getHitBox() {
        return hitBox;
    }

    /**
     * @return 攻击范围
     */
    public Sector<Integer> getAttackSector() {
        return attackSector;
    }

    /**
     * s
     *
     * @return 角色id
     */
    public int getRoleId() {
        return roleId;
    }

    public void bindBattleId(int battleId) {
        this.battleId = battleId;
    }

    public int getWeaponType() {
        return this.weaponType.getTypeId();
    }

    public void sendMessage(BaseMessage message) {
        Player player = PlayerService.getService().getPlayer(this.playerId);
        if (player != null) {
            player.sendMessage(message);
        }
    }

    public int getBattleId() {
        return battleId;
    }
}
