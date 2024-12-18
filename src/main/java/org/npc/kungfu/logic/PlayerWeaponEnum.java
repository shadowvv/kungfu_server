package org.npc.kungfu.logic;

enum PlayerWeaponEnum {

    /**
     * 剑
     */
    BLADE(3,1,1,1,1,1),
    /**
     * 刀
     */
    SWORD(3,1,1,1,1,1),
    /**
     * 长矛
     */
    SPEAR(3,1,1,1,1,1),
    /**
     * 匕首
     */
    KNIFE(3,1,1,1,1,1),
    /**
     * 弓
     */
    BOW(3,1,1,1,1,1);


    private final int moveRange;
    private final int attack;
    private final int attackInnerRadius;
    private final int attackOuterRadius;
    private final int startAngle;
    private final int endAngle;


    PlayerWeaponEnum(int moveRange,int attack,int attackInnerRadius,int attackOuterRadius,int startAngle,int endAngle) {
        this.moveRange = moveRange;
        this.attack =  attack;
        this.attackInnerRadius = attackInnerRadius;
        this.attackOuterRadius = attackOuterRadius;
        this.startAngle = startAngle;
        this.endAngle = endAngle;
    }

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
