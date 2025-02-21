package org.npc.kungfu.logic.constant;

import java.util.HashMap;
import java.util.Map;

import static org.npc.kungfu.logic.constant.BattleConstants.BASE_NUMBER;

public enum PlayerWeaponEnum {

    /**
     * 剑
     */
    BLADE(0, BASE_NUMBER * 3, 1, 0, BASE_NUMBER * 1.5f, 0, 90),
    /**
     * 刀
     */
    SWORD(1, BASE_NUMBER * 3, 1.5f, 0, BASE_NUMBER * 1.5f, 0, 60),
    /**
     * 长矛
     */
    SPEAR(2, BASE_NUMBER * 2, 1, BASE_NUMBER * 4, BASE_NUMBER * 5, 0, 45),
    /**
     * 匕首
     */
    KNIFE(3, BASE_NUMBER * 4, 2, 0, BASE_NUMBER * 1, 0, 90),
    /**
     * 弓
     */
    BOW(4, BASE_NUMBER * 4, 1, BASE_NUMBER * 3, BASE_NUMBER * 7, 0, 45),
    ;

    private static final Map<Integer, PlayerWeaponEnum> map = new HashMap<>();

    static {
        for (PlayerWeaponEnum weaponEnum : PlayerWeaponEnum.values()) {
            map.put(weaponEnum.getTypeId(), weaponEnum);
        }
    }

    public static PlayerWeaponEnum fromValue(int weaponType) {
        return map.get(weaponType);
    }

    private final int typeId;
    private final float moveRange;
    private final float attack;
    private final float attackInnerRadius;
    private final float attackOuterRadius;
    private final int startAngle;
    private final int endAngle;


    PlayerWeaponEnum(int typeId, float moveRange, float attack, float attackInnerRadius, float attackOuterRadius, int startAngle, int endAngle) {
        this.typeId = typeId;
        this.moveRange = moveRange;
        this.attack = attack;
        this.attackInnerRadius = attackInnerRadius;
        this.attackOuterRadius = attackOuterRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

    public int getTypeId() {
        return typeId;
    }

    public float getMoveRange() {
        return moveRange;
    }

    public float getAttack() {
        return attack;
    }

    public float getAttackInnerRadius() {
        return attackInnerRadius;
    }

    public float getAttackOuterRadius() {
        return attackOuterRadius;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public int getEndAngle() {
        return endAngle;
    }

}
