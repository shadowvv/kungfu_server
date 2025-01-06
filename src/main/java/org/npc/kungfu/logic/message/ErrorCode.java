package org.npc.kungfu.logic.message;

public enum ErrorCode {

    LOGIN_IS_LOGGING(1001),
    LOGIN_SAME_USERNAME(1002),

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
