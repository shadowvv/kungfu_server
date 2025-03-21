package org.npc.kungfu.database;

/**
 * 数据库玩家信息
 */
public class PlayerInfoEntity {

    private int id;
    private String userName;
    private String password;
    private String nickName;
    private int battleCount;
    private int winCount;
    private String weaponUseCount;
    private String weaponWinCount;

    public PlayerInfoEntity() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getBattleCount() {
        return battleCount;
    }

    public void setBattleCount(int battleCount) {
        this.battleCount = battleCount;
    }

    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    public String getWeaponUseCount() {
        return weaponUseCount;
    }

    public void setWeaponUseCount(String weaponUseCount) {
        this.weaponUseCount = weaponUseCount;
    }

    public String getWeaponWinCount() {
        return weaponWinCount;
    }

    public void setWeaponWinCount(String weaponWinCount) {
        this.weaponWinCount = weaponWinCount;
    }
}
