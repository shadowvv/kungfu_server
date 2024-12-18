package org.npc.kungfu.logic;

import org.npc.kungfu.platfame.math.HitBox;
import org.npc.kungfu.platfame.math.Sector;
import org.npc.kungfu.platfame.math.VectorTwo;

import static org.npc.kungfu.logic.BattleConstants.HIT_BOX_HEIGHT;
import static org.npc.kungfu.logic.BattleConstants.HIT_BOX_WIDTH;

public class Player {

    private final int playerId;
    private boolean active;

    private PlayerWeaponEnum weaponType;
    private PlayerActionTypeEnum actionType;
    private final int attack;
    private final int moveRange;


    private int hp;
    private VectorTwo<Integer> center;
    private HitBox<Integer> hitBox;
    private Sector<Integer> attackSector;
    private int faceAngle;

    Player(int playerId,PlayerWeaponEnum weaponType, boolean active,int positionX,int positionY,int faceAngle) {
        this.playerId = playerId;

        this.weaponType = weaponType;
        this.active = active;
        this.actionType = PlayerActionTypeEnum.WAIT;
        this.center = VectorTwo.createIntegerVector(positionX,positionY);
        this.faceAngle = faceAngle;

        this.attack = this.weaponType.getAttack();
        this.moveRange = this.weaponType.getMoveRange();
        this.hitBox = HitBox.createIntegerHitBox(positionX,positionY,HIT_BOX_WIDTH,HIT_BOX_HEIGHT);
        this.attackSector = Sector.createIntegerSector(positionX,positionY,this.weaponType.getAttackInnerRadius(),this.weaponType.getAttackOuterRadius(),this.weaponType.getStartAngle(),this.weaponType.getEndAngle());
    }

    public void onPlayerMove(int x,int y) {
        if (!this.active) {
            return;
        }

        if (this.center.inCirCle(x,y,moveRange)){
            return;
        }

        this.center.moveTo(x,y);
    }

    public void onPlayerHit(int faceAngle){
        if (!this.active) {
            return;
        }
        this.faceAngle = faceAngle;
    }

    public int onBeHit(int attack) {
        this.hp = this.hp - attack;
        if(this.hp < 0){
            this.hp = 0;
        }
        return this.hp;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive(){
        return active;
    }

    public int getAttack(){
        return this.attack;
    }

    public HitBox<Integer> getHitBox() {
        return hitBox;
    }

    public Sector<Integer> getAttackSector(){
        return attackSector;
    }

    public int getId() {
        return playerId;
    }

    public int getHp() {
        return hp;
    }
}
