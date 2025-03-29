package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

import java.util.Map;

public class PlayerInfoMessage {

    @Expose
    private long playerId;
    @Expose
    private String userName;
    @Expose
    private String nickName;
    @Expose
    private Map<Integer, Integer> weaponUseCountMap;
    @Expose
    private Map<Integer, Integer> weaponWinCountMap;

    public PlayerInfoMessage() {
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Map<Integer, Integer> getWeaponUseCountMap() {
        return weaponUseCountMap;
    }

    public void setWeaponUseCountMap(Map<Integer, Integer> weaponUseCountMap) {
        this.weaponUseCountMap = weaponUseCountMap;
    }

    public Map<Integer, Integer> getWeaponWinCountMap() {
        return weaponWinCountMap;
    }

    public void setWeaponWinCountMap(Map<Integer, Integer> weaponWinCountMap) {
        this.weaponWinCountMap = weaponWinCountMap;
    }
}
