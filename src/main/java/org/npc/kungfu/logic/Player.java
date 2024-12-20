package org.npc.kungfu.logic;

import org.npc.kungfu.logic.constant.PlayerWeaponEnum;
import org.npc.kungfu.logic.match.BattleMatchService;

public class Player {

    private final int playerId;
    private Role role;

    Player(int playerId) {
        this.playerId = playerId;
    }

    public void onPlayerApplyBattle(int weaponType) {
        if (role != null) {
            role.resetRole(weaponType);
        }
        role = Role.build(1,playerId,PlayerWeaponEnum.fromValue(weaponType),true,1,1,10);
        BattleMatchService.enterMatch(role);
    }

    public void onBattleOver(){

    }

    public int getPlayerId() {
        return playerId;
    }

    public void onPlayerLoginOut() {
    }
}
