package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class TeamMapBean implements Serializable{

    private int acceptNum;
    private int captainId;
    private String captainName;
    private int challengeNum;
    private int playerNum;

    public int getAcceptNum() {
        return acceptNum;
    }

    public void setAcceptNum(int acceptNum) {
        this.acceptNum = acceptNum;
    }

    public int getCaptainId() {
        return captainId;
    }

    public void setCaptainId(int captainId) {
        this.captainId = captainId;
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public int getChallengeNum() {
        return challengeNum;
    }

    public void setChallengeNum(int challengeNum) {
        this.challengeNum = challengeNum;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }
}
