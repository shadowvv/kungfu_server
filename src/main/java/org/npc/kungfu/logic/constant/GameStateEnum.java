package org.npc.kungfu.logic.constant;

/**
 * 游戏状态
 */
public enum GameStateEnum {

    /**
     * 登录
     */
    LOGIN(0),
    /**
     * 选择武器
     */
    CHOOSE_WEAPON(1),
    /**
     * 准备
     */
    PREPARE(2),
    /**
     * 等待指令
     */
    WAIT_COMMAND(3),
    /**
     * 行动
     */
    ACTION(4),
    /**
     * 行动倒计时
     */
    WAIT_ACTION(5),
    /**
     * 结束
     */
    END(6),
    ;;
    private final int code;

    private GameStateEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
