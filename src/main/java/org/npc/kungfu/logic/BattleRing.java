package org.npc.kungfu.logic;

import org.npc.kungfu.platfame.LogicMessage;
import org.npc.kungfu.platfame.math.GeometricAlgorithms;
import org.npc.kungfu.platfame.math.HitBox;
import org.npc.kungfu.platfame.math.Sector;

import java.util.ArrayList;
import java.util.HashMap;

import static org.npc.kungfu.logic.BattleConstants.WAIT_COMMAND_TICK;

public class BattleRing {

    private HashMap<Integer,Player> players;
    private ArrayList<LogicMessage> messageList;

    private long tick = 0;
    private long lastTick = 0;
    private long countDownTick = 0;

    private GameStateEnum gameState = GameStateEnum.PREPARE;

    private BattleRing(){
        players = new HashMap<>();
        messageList = new ArrayList<>();
    }

    private void checkHit(){
        for (Player player : players.values()) {
            for (Player otherPlayer : players.values()) {
                if (player.getId() != otherPlayer.getId() && checkHit(player.getAttackSector(),otherPlayer.getHitBox())){
                    otherPlayer.onBeHit(player.getAttack());
                }
            }
        }
    }

    private boolean checkHit(Sector<Integer> attackSector, HitBox<Integer> hitBox){
        return GeometricAlgorithms.isSectorCollidingWithRect(attackSector,hitBox);
    }

    public void update(long deltaTime) {
        if(this.gameState == GameStateEnum.PREPARE || this.gameState == GameStateEnum.END){
            return;
        }

        this.tick += deltaTime;
        if (this.tick - this.lastTick < 1) {
            return;
        }
        this.lastTick = this.tick;

        if(this.gameState == GameStateEnum.WAIT_COMMAND || this.gameState == GameStateEnum.WAIT_OTHER){
            this.countDownTick = this.countDownTick - 1;
            if(this.countDownTick <= 0){
                this.gameState = GameStateEnum.ACTION;

                checkHit();
                for (Player player : players.values()) {
                    if (player.getHp() == 0) {
                        this.gameState = GameStateEnum.END;
                        break;
                    }
                }
                this.gameState = GameStateEnum.WAIT_COMMAND;
                this.countDownTick = WAIT_COMMAND_TICK;
            }
        }
    }


}
