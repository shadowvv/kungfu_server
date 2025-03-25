package org.npc.kungfu.logic.message;

public enum ErrorCode {

    LOGIN_IS_LOGGING(1001),
    LOGIN_SAME_USERNAME(1002),
    LOGIN_CHANNEL_BIND_PLAYER(1003),
    LOGIN_USERNAME_PASSWORD_ERROR(1004),

    PLAYER_IN_BATTLE(1004),
    PLAYER_IN_MATCH(1005),
    PLAYER_NOT_IN_MATCH(1006),
    MATCH_TIMEOUT(1007),

    SYSTEM_ERROR(9999),

    ;
    private final int code;

    private ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
