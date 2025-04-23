package org.npc.kungfu.logic.constant;

/**
 * 游戏状态
 */
public enum BattleStateEnum {
    /**
     * 准备
     */
    PREPARE(1),
    /**
     * 等待指令
     */
    WAIT_COMMAND(2),
    /**
     * 行动
     */
    ACTION(3),
    /**
     * 行动倒计时
     */
    WAIT_CLIENT_ACTION(4),
    /**
     * 结束
     */
    END(5),
    ;;
    private final int code;

    private BattleStateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
