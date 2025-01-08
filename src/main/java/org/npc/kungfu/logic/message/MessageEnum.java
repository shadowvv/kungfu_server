package org.npc.kungfu.logic.message;

import org.npc.kungfu.net.LogicMessage;

import java.util.HashMap;
import java.util.Map;

public enum MessageEnum {

    LOGIN_REQ(1001, LoginReqMessage.class),
    LOGIN_RESP(1002, LoginRespMessage.class),

    APPLY_BATTLE_REQ(2001, ApplyBattleReqMessage.class),
    APPLY_BATTLE_RESP(2002, ApplyBattleRespMessage.class),

    OPERATION_REQ(3001, OperationReqMessage.class),
    OPERATION_RESP(3002, OperationRespMessage.class),

    ERROR_MESSAGE(9999, ErrorMessage.class),

    MATCH_RESULT_BROAD(10001, MatchResultBroadMessage.class),
    BATTLE_RESULT_BROAD(10002, BattleResultBroadMessage.class),

    SS_PLAYER_CHANNEL_INACTIVE(20001, SSPlayerChannelInactive.class),
    SS_PLAYER_CHANNEL_RECONNECT(20002, SSPlayerChannelReconnect.class),
    SS_CREATE_BATTLE(20012, SSCreateBattleMessage.class),
    ;

    private static final Map<Integer, MessageEnum> map = new HashMap<>();

    static {
        for (MessageEnum messageEnum : MessageEnum.values()) {
            map.put(messageEnum.getId(), messageEnum);
        }
    }

    public static MessageEnum fromValue(int messageId) {
        return map.get(messageId);
    }

    private final int id;
    private final Class<? extends LogicMessage> clazz;

    MessageEnum(int id, Class<? extends LogicMessage> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public Class<? extends LogicMessage> getClazz() {
        return clazz;
    }
}
