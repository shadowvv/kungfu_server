package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.message.MatchResultBroadMessage;
import org.npc.kungfu.logic.message.RoleMessage;
import org.npc.kungfu.platfame.bus.IBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchPool implements IBus<Role> {

    private final ConcurrentLinkedQueue<Role> roles;
    private final AtomicInteger roleNum;
    private final String signature;

    public MatchPool(String signature) {
        roles = new ConcurrentLinkedQueue<>();
        roleNum = new AtomicInteger(0);
        this.signature = signature;
    }

    @Override
    public boolean put(Role passenger) {
        roles.add(passenger);
        roleNum.incrementAndGet();
        return true;
    }

    @Override
    public String getSignature() {
        return this.signature;
    }

    @Override
    public int getPassengerCount() {
        return roleNum.get();
    }

    @Override
    public Boolean call() throws Exception {
        matchUp();
        return true;
    }

    private void matchUp() {
        if (roles.isEmpty()) {
            return;
        }
        if (roleNum.intValue() <= 1) {
            return;
        }
        for (int i = 0; i < 10; i++) {
            List<Role> list = new ArrayList<>();

            Role role1 = roles.poll();
            assert role1 != null;
            role1.resetPosition(-200,0,0);

            Role role2 = roles.poll();
            assert role2 != null;
            role2.resetPosition(200,0,0);

            list.add(role1);
            list.add(role2);
            roleNum.decrementAndGet();
            roleNum.decrementAndGet();

            MatchResultBroadMessage matchResultBroadMessage = buildMatchResultBroadMessage(list);
            role1.sendMessage(matchResultBroadMessage);
            role2.sendMessage(matchResultBroadMessage);
            BattleService.getService().startBattle(list);

            if (roles.isEmpty()) {
                return;
            }
            if (roleNum.intValue() <= 1) {
                return;
            }
        }
    }

    private MatchResultBroadMessage buildMatchResultBroadMessage(List<Role> list) {
        MatchResultBroadMessage matchResultBroadMessage = new MatchResultBroadMessage();
        List<RoleMessage> roleMessages = new ArrayList<>();
        for (Role role : list) {
            RoleMessage roleMessage = new RoleMessage();
            roleMessage.setRoleId(role.getRoleId());
            roleMessage.setUserName(role.getUserName());
            roleMessage.setWeaponType(role.getWeaponType());
            roleMessage.setX(role.getCenter().getX());
            roleMessage.setY(role.getCenter().getY());
            roleMessage.setFaceAngle(role.getFaceAngle());
            roleMessages.add(roleMessage);
        }
        matchResultBroadMessage.setRoles(roleMessages);
        return matchResultBroadMessage;
    }
}
