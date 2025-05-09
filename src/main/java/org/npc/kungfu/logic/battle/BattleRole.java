package org.npc.kungfu.logic.battle;

import org.npc.kungfu.logic.BaseRole;
import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.constant.PlayerActionTypeEnum;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.platfame.math2.HitBox;
import org.npc.kungfu.platfame.math2.Sector;
import org.npc.kungfu.platfame.math2.Vec2;

import static org.npc.kungfu.logic.constant.BattleConstants.HIT_BOX_HEIGHT;
import static org.npc.kungfu.logic.constant.BattleConstants.HIT_BOX_WIDTH;

/**
 * 玩家操作的角色
 */
//TODO:将匹配拆成匹配角色
public class BattleRole extends BaseRole {

    /**
     * 角色所在战斗id
     */
    private long battleId;
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
    private final float attackPoint;
    /**
     * 角色移动范围
     */
    private final float moveRange;
    /**
     * 角色生命值
     */
    private float hpPoint;

    /**
     * 角色中心点
     */
    private Vec2 center;
    /**
     * 角色受击盒
     */
    private final HitBox hitBox;
    /**
     * 角色攻击范围
     */
    private final Sector attackSector;
    /**
     * 角色朝向
     */
    private double faceAngle;



    /**
     * 角色工厂
     *
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器
     * @param positionX  位置x坐标
     * @param positionY  位置y坐标
     * @param faceAngle  朝向
     * @return 战斗角色
     */
    public static BattleRole build(int roleId, int playerId, PlayerWeaponEnum weaponType, double positionX, double positionY, double faceAngle) {
        return new BattleRole(roleId, playerId, weaponType, positionX, positionY, faceAngle);
    }

    /**
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器
     * @param positionX  位置x坐标
     * @param positionY  位置y坐标
     * @param faceAngle  朝向
     */
    private BattleRole(int roleId, int playerId, PlayerWeaponEnum weaponType, double positionX, double positionY, double faceAngle) {
        super(roleId, playerId);
        this.weaponType = weaponType;
        this.actionType = PlayerActionTypeEnum.MOVE;
        this.center = new Vec2(positionX, positionY);
        this.faceAngle = faceAngle;

        this.hpPoint = 5;
        this.active = true;

        this.attackPoint = this.weaponType.getAttack();
        this.moveRange = this.weaponType.getMoveRange();
        this.hitBox = new HitBox(new Vec2(positionX - HIT_BOX_WIDTH / 2.0f, positionY - HIT_BOX_HEIGHT / 2.0f), HIT_BOX_WIDTH, HIT_BOX_HEIGHT);
        this.attackSector = new Sector(new Vec2(positionX, positionY), this.weaponType.getAttackInnerRadius(), this.weaponType.getAttackOuterRadius(), this.weaponType.getStartAngle(), this.weaponType.getEndAngle());
    }

    /**
     * 重置位置
     *
     * @param x         位置x坐标
     * @param y         位置y坐标
     * @param faceAngle 朝向
     */
    public void resetPosition(int x, int y, int faceAngle) {
        this.center = new Vec2(x, y);
        this.attackSector.setCenter(this.center);
        this.hitBox.setLeftTop(x - HIT_BOX_WIDTH / 2.0, y - HIT_BOX_HEIGHT / 2.0);
        this.faceAngle = faceAngle;
    }

    /**
     * 重置角色武器
     *
     * @param weaponType 角色武器
     */
    public void resetRole(PlayerWeaponEnum weaponType) {
        this.weaponType = weaponType;
    }

    /**
     * 角色移动
     *
     * @param x 目标x坐标
     * @param y 目标y坐标
     */
    public boolean onRoleMove(double x, double y) {
        if (!this.active) {
            return false;
        }

        if (this.actionType != PlayerActionTypeEnum.MOVE) {
            return false;
        }

        if (!this.center.inCirCle(x, y, moveRange)) {
            return false;
        }

        this.center = new Vec2(x, y);
        this.attackSector.setCenter(this.center);
        this.hitBox.setLeftTop(this.center.getX() - HIT_BOX_WIDTH / 2.0, this.center.getY() - HIT_BOX_HEIGHT / 2.0);

        this.actionType = PlayerActionTypeEnum.ATTACK;
        return true;
    }

    /**
     * 角色转向
     *
     * @param faceAngle 角色朝向
     * @return 转向是否成功
     */
    public boolean onRoleHit(double faceAngle) {
        if (!this.active) {
            return false;
        }
        if (this.actionType != PlayerActionTypeEnum.ATTACK) {
            return false;
        }
        this.faceAngle = faceAngle;
        this.attackSector.updateAngle(this.faceAngle);
        this.actionType = PlayerActionTypeEnum.MOVE;
        return true;
    }

    /**
     * 角色被击中
     *
     * @param attackPoint 攻击力
     * @return 剩余血量
     */
    public float onRoleBeHit(float attackPoint) {
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
    public float getAttackPoint() {
        return this.attackPoint;
    }

    /**
     * @return 生命值
     */
    public float getHpPoint() {
        return hpPoint;
    }

    /**
     * @return 角色受击盒
     */
    public HitBox getHitBox() {
        return hitBox;
    }

    /**
     * @return 攻击范围
     */
    public Sector getAttackSector() {
        return attackSector;
    }

    /**
     * 绑定战斗id
     *
     * @param battleId 战斗id
     */
    public void bindBattleId(long battleId) {
        this.battleId = battleId;
    }

    /**
     * @return 角色绑定的战斗id
     */
    public long getBattleId() {
        return battleId;
    }

    /**
     * @return 角色使用的武器
     */
    public PlayerWeaponEnum getWeaponType() {
        return this.weaponType;
    }

    /**
     * @return 用户名
     */
    public String getUserName() {
        Player player = PlayerService.getService().getPlayer(this.getPlayerId());
        if (player != null) {
            return player.getUserName();
        }
        return "";
    }

    /**
     * @return 角色中心点
     */
    public Vec2 getCenter() {
        return this.center;
    }

    /**
     * @return 角色朝向
     */
    public double getFaceAngle() {
        return this.faceAngle;
    }
}
