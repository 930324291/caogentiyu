package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class ScoreListBean extends MineBean implements Serializable {
    private String address;
    private long beginTime;
    private long createTime;
    private TeamBean teamA;
    private TeamBean teamB;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public TeamBean getTeamA() {
        return teamA;
    }

    public void setTeamA(TeamBean teamA) {
        this.teamA = teamA;
    }

    public TeamBean getTeamB() {
        return teamB;
    }

    public void setTeamB(TeamBean teamB) {
        this.teamB = teamB;
    }
}
