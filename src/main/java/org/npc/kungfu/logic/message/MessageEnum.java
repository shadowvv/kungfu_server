package org.npc.kungfu.logic.message;

import org.npc.kungfu.net.LogicMessage;

import java.util.HashMap;
import java.util.Map;

public enum MessageEnum {

    LOGIN_REQ(1001, LoginReqMessage.class),
    LOGIN_RESP(1002, LoginRespMessage.class),
    REGISTER_REQ(1003, RegisterReqMessage.class),
    REGISTER_RESP(1004, RegisterRespMessage.class),
    RENAME_REQ(1005, RenameReqMessage.class),
    RENAME_RESP(1006, RenameRespMessage.class),

    APPLY_BATTLE_REQ(2001, ApplyBattleReqMessage.class),
    APPLY_BATTLE_RESP(2002, ApplyBattleRespMessage.class),

    CANCEL_MATCH_REQ(3001, CancelMatchReqMessage.class),
    CANCEL_MATCH_RESP(3002, CancelMatchRespMessage.class),
    BATTLE_LOAD_READY_REQ(5001, LoadBattleReadyReqMessage.class),
    BATTLE_LOAD_READY_RESP(5002, LoadBattleReadyRespMessage.class),

    OPERATION_REQ(4001, OperationReqMessage.class),
    OPERATION_RESP(4002, OperationRespMessage.class),

    ERROR_MESSAGE(9999, ErrorMessage.class),

    MATCH_RESULT_BROAD(10001, MatchResultBroadMessage.class),
    BATTLE_START_BROAD_MESSAGE(10002, BattleStartBroadMessage.class),
    BATTLE_RESULT_BROAD(10003, BattleResultBroadMessage.class),
    BATTLE_STATE_BROAD(10004, BattleStateBroadMessage.class),

    SS_PLAYER_CHANNEL_INACTIVE(20001, SSPlayerChannelInactive.class),
    SS_PLAYER_CHANNEL_RECONNECT(20002, SSPlayerChannelReconnect.class),
    SS_ENTER_MATCH(20003, SSEnterMatch.class),
    SS_LOGOUT_TO_MATCH(20004, SSPlayerLogoutToMatch.class),
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
