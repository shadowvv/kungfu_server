package org.npc.kungfu.logic;

/**
 * 游戏状态
 */
enum GameStateEnum {

    /**
     * 准备
     */
    PREPARE,
    /**
     * 等待指令
     */
    WAIT_COMMAND,
    /**
     * 等待其他人
     */
    WAIT_OTHER,
    /**
     * 行动
     */
    ACTION,
    /**
     * 结束
     */
    END,

}
