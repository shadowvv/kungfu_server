package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.BaseRole;
import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.platfame.math2.Vec2;

/**
 * 匹配角色信息
 */
public class MatchRole extends BaseRole {

    /**
     * 玩家昵称
     */
    private final String username;
    /**
     * 角色使用的武器
     */
    private final PlayerWeaponEnum weaponType;
    /**
     * 进去匹配的时间
     */
    private long enterMatchTime;

    /**
     * 角色所在战斗id
     */
    private int battleId;
    /**
     * 角色中心点
     */
    private Vec2 center;
    /**
     * 角色朝向
     */
    private double faceAngle;

    /**
     * 构建匹配角色
     *
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器类型
     * @return 匹配角色
     */
    public static MatchRole build(int roleId, int playerId, String username, PlayerWeaponEnum weaponType) {
        return new MatchRole(roleId, playerId, username, weaponType);
    }

    /**
     * @param roleId     角色id
     * @param playerId   玩家id
     * @param weaponType 武器类型
     */
    private MatchRole(int roleId, int playerId, String username, PlayerWeaponEnum weaponType) {
        super(roleId, playerId);
        this.username = username;
        this.weaponType = weaponType;
    }

    /**
     * 角色进入匹配
     */
    public void enterMatch() {
        this.enterMatchTime = System.currentTimeMillis();
    }

    /**
     * 重置角色位置
     *
     * @param x         x坐标
     * @param y         y坐标
     * @param faceAngle 朝向
     */
    public void resetPosition(double x, double y, double faceAngle) {
        this.center = new Vec2(x, y);
        this.faceAngle = faceAngle;
    }

    /**
     * 获取角色昵称
     *
     * @return 角色昵称
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取角色武器类型
     *
     * @return 角色武器类型
     */
    public PlayerWeaponEnum getWeaponType() {
        return weaponType;
    }

    /**
     * 获取角色进入匹配时间
     *
     * @return 角色进入匹配时间
     */
    public long getEnterMatchTime() {
        return enterMatchTime;
    }

    /**
     * 获取角色所在战斗id
     *
     * @return 角色所在战斗id
     */
    public int getBattleId() {
        return battleId;
    }

    /**
     * 获取角色中心点
     *
     * @return 角色中心点
     */
    public Vec2 getCenter() {
        return center;
    }

    /**
     * 获取角色朝向
     *
     * @return 角色朝向
     */
    public double getFaceAngle() {
        return faceAngle;
    }
}
