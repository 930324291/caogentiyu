package com.football.net.bean;

import com.football.net.common.util.StringUtils;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class GameBean extends MineBean implements Serializable {
    private String title;
    private String address;
    private String content;
    private long beginTime;
    private long createTime;
    private TeamBean teamA;
    private TeamBean team;
    private TeamBean teamB;
    private String scoreA;
    private String scoreB;
    private String scoreA1;
    private String scoreB1;
    private String scoreA2;
    private String scoreB2;
    private String teamType;

    //1 已应战 2已拒绝 其他等待应战中...
    private Integer teamBOperation;
    private int id;

    private Integer auditStatus; //审核状态 auditStatusList

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public Integer getTeamBOperation() {
        return teamBOperation;
    }

    public void setTeamBOperation(Integer teamBOperation) {
        this.teamBOperation = teamBOperation;
    }

    public String getContent() {
        if (StringUtils.isEmpty(content)) {
            content = "";
        }
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
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
//        if (null == teamB){
//            teamB = new TeamBean();
//        }
        return teamB;
    }

    public void setTeamB(TeamBean teamB) {
        this.teamB = teamB;
    }

    public String getScoreA() {
        return scoreA;
    }

    public void setScoreA(String scoreA) {
        this.scoreA = scoreA;
    }

    public String getScoreB() {
        return scoreB;
    }

    public void setScoreB(String scoreB) {
        this.scoreB = scoreB;
    }

    public String getScoreA1() {
        return scoreA1;
    }

    public void setScoreA1(String scoreA1) {
        this.scoreA1 = scoreA1;
    }

    public String getScoreB1() {
        return scoreB1;
    }

    public void setScoreB1(String scoreB1) {
        this.scoreB1 = scoreB1;
    }

    public String getScoreA2() {
        return scoreA2;
    }

    public void setScoreA2(String scoreA2) {
        this.scoreA2 = scoreA2;
    }

    public String getScoreB2() {
        return scoreB2;
    }

    public void setScoreB2(String scoreB2) {
        this.scoreB2 = scoreB2;
    }
}
