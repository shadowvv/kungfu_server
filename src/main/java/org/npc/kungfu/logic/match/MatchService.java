package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Role;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.message.*;
import org.npc.kungfu.platfame.bus.ITaskStation;
import org.npc.kungfu.platfame.bus.TaskStation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchService {

    private static final MatchService service = new MatchService();

    public static MatchService getService() {
        return service;
    }

    private MatchService() {
    }

    private final ConcurrentLinkedQueue<Role> roles = new ConcurrentLinkedQueue<>();
    private final AtomicInteger roleNum = new AtomicInteger(0);
    private ITaskStation taskStation;

    public void init(TaskStation matchStation) {
        this.taskStation = matchStation;
    }

    public void enterMatch(Role role) {
        roles.add(role);
        roleNum.incrementAndGet();
        putMessage(new SSStartMatchUpMessage(), 0);
    }

    public void matchUp() {
        if (roles.isEmpty()) {
            return;
        }
        if (roleNum.intValue() <= 1){
            return;
        }
        for (int i = 0; i < 10; i++) {
            List<Role> list = new ArrayList<>();
            Role role1 = roles.poll();
            Role role2 = roles.poll();
            list.add(role1);
            list.add(role2);
            roleNum.decrementAndGet();
            roleNum.decrementAndGet();

            MatchResultBroadMessage matchResultBroadMessage = buildMatchResultBroadMessage(list);
            assert role1 != null;
            role1.sendMessage(matchResultBroadMessage);
            assert role2 != null;
            role2.sendMessage(matchResultBroadMessage);
            BattleService.getService().putMessage(new SSCreateBattleMessage(list),0);
            if (roles.isEmpty()) {
                return;
            }
            if (roleNum.intValue() <= 1){
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
            roleMessage.setWeaponType(role.getWeaponType());
            roleMessages.add(roleMessage);
        }
        matchResultBroadMessage.setRoles(roleMessages);
        return matchResultBroadMessage;
    }

    public void putMessage(BaseMessage msg, int player) {
        this.taskStation.putMessage(msg);
    }
}
