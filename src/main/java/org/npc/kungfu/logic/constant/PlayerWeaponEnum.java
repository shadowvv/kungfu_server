package org.npc.kungfu.logic.constant;

import java.util.HashMap;
import java.util.Map;

public enum PlayerWeaponEnum {

    /**
     * 剑
     */
    BLADE(0,3,1,1,1,1,1),
    /**
     * 刀
     */
    SWORD(1,3,1,1,1,1,1),
    /**
     * 长矛
     */
    SPEAR(2,3,1,1,1,1,1),
    /**
     * 匕首
     */
    KNIFE(3,3,1,1,1,1,1),
    /**
     * 弓
     */
    BOW(4,3,1,1,1,1,1),
    ;

    private static final Map<Integer,PlayerWeaponEnum> map = new HashMap<>();
    static {
        for (PlayerWeaponEnum weaponEnum: PlayerWeaponEnum.values()) {
            map.put(weaponEnum.getTypeId(),weaponEnum);
        }
    }

    public static PlayerWeaponEnum fromValue(int weaponType){
        return map.get(weaponType);
    }

    private final int typeId;
    private final int moveRange;
    private final int attack;
    private final int attackInnerRadius;
    private final int attackOuterRadius;
    private final int startAngle;
    private final int endAngle;


    PlayerWeaponEnum(int typeId,int moveRange,int attack,int attackInnerRadius,int attackOuterRadius,int startAngle,int endAngle) {
        this.typeId = typeId;
        this.moveRange = moveRange;
        this.attack =  attack;
        this.attackInnerRadius = attackInnerRadius;
        this.attackOuterRadius = attackOuterRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public int getTypeId() {return typeId;}

    public int getMoveRange() {
        return moveRange;
    }

    public int getAttack() {
        return attack;
    }

    public int getAttackInnerRadius() {
        return attackInnerRadius;
    }

    public int getAttackOuterRadius() {
        return attackOuterRadius;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public int getEndAngle() {
        return endAngle;
    }

}
