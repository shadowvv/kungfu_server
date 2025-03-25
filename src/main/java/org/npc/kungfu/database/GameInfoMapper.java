package org.npc.kungfu.database;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface GameInfoMapper {

    @Select("SELECT nextPlayerId FROM game_param WHERE id = 1")
    GameInfoEntity getGameInfo();

    @Update("UPDATE game_param SET nextPlayerId = #{nextPlayerId} WHERE id = 1")
    void updateGameInfo(@Param("nextPlayerId") int nextPlayerId);
}
