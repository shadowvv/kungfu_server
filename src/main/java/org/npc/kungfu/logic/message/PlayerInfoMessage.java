package org.npc.kungfu.logic.message;

import com.google.gson.annotations.Expose;

public class PlayerInfoMessage {

    @Expose
    private long playerId;
    @Expose
    private String userName;
    @Expose
    private int favouriteWeapon;
    @Expose
    private int winRate;
    @Expose
    private int bladeRate;
    @Expose
    private int swordRate;
    @Expose
    private int spearRate;
    @Expose
    private int bowRate;
    @Expose
    private int knifeRate;

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

    public int getFavouriteWeapon() {
        return favouriteWeapon;
    }

    public void setFavouriteWeapon(int favouriteWeapon) {
        this.favouriteWeapon = favouriteWeapon;
    }

    public int getWinRate() {
        return winRate;
    }

    public void setWinRate(int winRate) {
        this.winRate = winRate;
    }

    public int getBladeRate() {
        return bladeRate;
    }

    public void setBladeRate(int bladeRate) {
        this.bladeRate = bladeRate;
    }

    public int getSwordRate() {
        return swordRate;
    }

    public void setSwordRate(int swordRate) {
        this.swordRate = swordRate;
    }

    public int getSpearRate() {
        return spearRate;
    }

    public void setSpearRate(int spearRate) {
        this.spearRate = spearRate;
    }

    public int getBowRate() {
        return bowRate;
    }

    public void setBowRate(int bowRate) {
        this.bowRate = bowRate;
    }

    public int getKnifeRate() {
        return knifeRate;
    }

    public void setKnifeRate(int knifeRate) {
        this.knifeRate = knifeRate;
    }
}
