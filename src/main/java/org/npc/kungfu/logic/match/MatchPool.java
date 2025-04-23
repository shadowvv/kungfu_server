package org.npc.kungfu.logic.match;

import org.npc.kungfu.logic.Player;
import org.npc.kungfu.logic.PlayerService;
import org.npc.kungfu.logic.battle.BattleService;
import org.npc.kungfu.logic.message.ErrorCode;
import org.npc.kungfu.logic.message.ErrorMessage;
import org.npc.kungfu.logic.message.MatchResultBroadMessage;
import org.npc.kungfu.logic.message.PlayerInfoMessage;
import org.npc.kungfu.logic.message.base.BaseMessage;
import org.npc.kungfu.platfame.bus.SimplePassenger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 匹配池
 */
public class MatchPool extends SimplePassenger<BaseMessage> {

    /**
     * 角色列表
     */
    private final ConcurrentLinkedDeque<MatchRole> roles;
    private final ConcurrentHashMap<Integer, MatchRole> matchRoleMap;
    /**
     * 匹配池玩家数量
     */
    private final AtomicInteger roleNum;
    /**
     * 匹配池描述
     */
    private final String description;

    /**
     * @param id          匹配池id
     * @param description 描述
     */
    public MatchPool(long id, String description) {
        super(id);
        this.matchRoleMap = new ConcurrentHashMap<>();
        this.roles = new ConcurrentLinkedDeque<>();
        this.roleNum = new AtomicInteger(0);
        this.description = description;
    }

    /**
     * 进入匹配
     * @param role 角色
     */
    public void enterMatch(MatchRole role) {
        matchRoleMap.put(role.getPlayerId(), role);
        roles.add(role);
        roleNum.incrementAndGet();
        role.enterMatch();
    }

    /**
     * 取消匹配
     *
     * @param roleId 角色 id
     * @return 是否成功
     */
    public boolean cancelMatch(int roleId) {
        MatchRole role = matchRoleMap.remove(roleId);
        if (role == null) {
            return false;
        }
        boolean result = roles.remove(role);
        if (result) {
            roleNum.decrementAndGet();
        }
        return result;
    }

    @Override
    public void heartbeat() {
        matchUp();
    }

    /**
     * 匹配
     */
    private void matchUp() {
        while (roleNum.intValue() >= 2) {
            MatchRole role1 = roles.poll();
            if (role1 == null) break;
            if (System.currentTimeMillis() - role1.getEnterMatchTime() > 60 * 1000) {
                role1.sendMessage(new ErrorMessage(2001, ErrorCode.MATCH_TIMEOUT.getCode()));
                continue;
            }
            role1.resetPosition(550, 360, 0);

            MatchRole role2 = roles.poll();
            if (role2 == null) {
                roles.addFirst(role1);
                break;
            }
            role2.resetPosition(650, 360, 180);
            if (System.currentTimeMillis() - role2.getEnterMatchTime() > 60 * 1000) {
                role2.sendMessage(new ErrorMessage(2001, ErrorCode.MATCH_TIMEOUT.getCode()));
                roles.addFirst(role1);
                continue;
            }

            List<MatchRole> list = List.of(role1, role2);
            roleNum.addAndGet(-2);

            MatchResultBroadMessage matchResultBroadMessage = buildMatchResultBroadMessage(list);
            role1.sendMessage(matchResultBroadMessage);
            role2.sendMessage(matchResultBroadMessage);

            BattleService.getService().startBattle(list);
        }
    }


    /**
     * 发送匹配结果
     * @param list 匹配战斗的玩家
     * @return 匹配结果消息
     */
    private MatchResultBroadMessage buildMatchResultBroadMessage(List<MatchRole> list) {
        MatchResultBroadMessage matchResultBroadMessage = new MatchResultBroadMessage();
        List<PlayerInfoMessage> playerInfoMessages = new ArrayList<>();
        for (MatchRole role : list) {
            Player player = PlayerService.getService().getPlayer(role.getPlayerId());
            playerInfoMessages.add(player.getPlayerInfo());
        }
        matchResultBroadMessage.setPlayerInfoList(playerInfoMessages);
        return matchResultBroadMessage;
    }

    @Override
    public String description() {
        return this.description;
    }
}
