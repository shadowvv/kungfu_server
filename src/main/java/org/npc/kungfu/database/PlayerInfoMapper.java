package org.npc.kungfu.database;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface PlayerInfoMapper {

    @Insert("INSERT INTO player_info(id, userName, password, nickName, weaponUseCount, weaponWinCount) " +
            "VALUES (#{id}, #{userName}, #{password}, #{nickName}, #{weaponUseCount}, #{weaponWinCount})")
    void insertPlayerInfo(PlayerInfoEntity playerInfo);

    @Select("SELECT id, nickName, weaponUseCount, weaponWinCount " +
            "FROM player_info WHERE userName = #{userName} AND password = #{password}")
    PlayerInfoEntity getPlayerInfo(@Param("userName") String userName, @Param("password") String password);

    @Select("SELECT id FROM player_info WHERE userName = #{userName}")
    Integer getPlayerId(@Param("userName") String userName);

    @Select("SELECT player_info.userName FROM player_info")
    List<String> getAllPlayerUserNames();

    @Update("UPDATE player_info SET nickName = #{nickName} WHERE id = #{playerId}")
    void updatePlayerNickName(@Param("playerId") Integer playerId, @Param("nickName") String nickName);

    @Update("UPDATE player_info SET nickName = #{nickName}" +
            "weaponUseCount = #{weaponUseCount}, weaponWinCount = #{weaponWinCount} WHERE id = #{id}")
    void updatePlayerWeaponData(PlayerInfoEntity playerInfo);

}
