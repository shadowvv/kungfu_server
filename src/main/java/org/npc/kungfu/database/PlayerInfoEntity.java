package org.npc.kungfu.database;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据库玩家信息
 */
public class PlayerInfoEntity {

    private int id;
    private String userName;
    private String nickName;
    private String password;
    private String weaponUseCount;
    private String weaponWinCount;

    private final HashMap<Integer, Integer> weaponUseCountMap = new HashMap<>();
    private final HashMap<Integer, Integer> weaponWinCountMap = new HashMap<>();

    public PlayerInfoEntity(int id, String userName, String nickName, String password,
                            String weaponUseCount, String weaponWinCount) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.weaponUseCount = weaponUseCount;
        this.weaponWinCount = weaponWinCount;
    }

    public PlayerInfoEntity(int id, String userName, String nickName, String password) {
        this.id = id;
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.weaponUseCount = "[]";
        this.weaponWinCount = "[]";
    }

    public void parseWeaponJson() {
        try {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<WeaponCount>>() {
            }.getType();
            List<WeaponCount> useCountList = gson.fromJson(this.weaponUseCount, listType);
            for (WeaponCount item : useCountList) {
                weaponUseCountMap.put(item.getType(), item.getCount());
            }

            List<WeaponCount> winCountList = gson.fromJson(this.weaponWinCount, listType);
            for (WeaponCount item : winCountList) {
                weaponWinCountMap.put(item.getType(), item.getCount());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PlayerInfoEntity buildWeaponJson() {
        try {
            Gson gson = new Gson();
            List<WeaponCount> useCountList = weaponUseCountMap.entrySet().stream()
                    .map(entry -> new WeaponCount(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            this.weaponUseCount = gson.toJson(useCountList);

            List<WeaponCount> winCountList = weaponWinCountMap.entrySet().stream()
                    .map(entry -> new WeaponCount(entry.getKey(), entry.getValue()))
                    .collect(Collectors.toList());
            this.weaponWinCount = gson.toJson(winCountList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
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

    public HashMap<Integer, Integer> getWeaponUseCountMap() {
        return weaponUseCountMap;
    }

    public HashMap<Integer, Integer> getWeaponWinCountMap() {
        return weaponWinCountMap;
    }

    private static class WeaponCount {
        private final int type;
        private final int count;

        public WeaponCount(int type, int count) {
            this.type = type;
            this.count = count;
        }

        public int getType() {
            return type;
        }

        public int getCount() {
            return count;
        }
    }
}