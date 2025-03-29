package org.npc.kungfu.database;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PlayerInfoMapper {

    @Insert("INSERT INTO player_info(id, userName, password, nickName, battleCount, winCount, weaponUseCount, weaponWinCount) " +
            "VALUES (#{id}, #{userName}, #{password}, #{nickName}, #{battleCount}, #{winCount}, #{weaponUseCount}, #{weaponWinCount})")
    void insertPlayerInfo(@Param("id") Integer id, @Param("userName") String userName, @Param("password") String password,
                          @Param("nickName") String nickName, @Param("battleCount") Integer battleCount,
                          @Param("winCount") Integer winCount, @Param("weaponUseCount") String weaponUseCount,
                          @Param("weaponWinCount") String weaponWinCount);

    @Select("SELECT id, nickName, battleCount, winCount, weaponUseCount, weaponWinCount " +
            "FROM player_info WHERE userName = #{userName} AND password = #{password}")
    PlayerInfoEntity getPlayerInfo(@Param("userName") String userName, @Param("password") String password);

    @Select("SELECT id FROM player_info WHERE userName = #{userName}")
    Integer getPlayerId(@Param("userName") String userName);

    @Select("SELECT player_info.userName FROM player_info")
    List<String> getAllPlayerUserNames();

    @Update("UPDATE player_info SET nickName = #{nickName} WHERE id = #{playerId}")
    void updatePlayerInfo(@Param("playerId") Integer playerId, @Param("nickName") String nickName);

}
