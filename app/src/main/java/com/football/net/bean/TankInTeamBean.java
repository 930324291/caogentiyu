package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class TankInTeamBean implements Serializable{

    private int applyId;
    private int id;
    private TeamBean team;
    private Integer confirmStatus; // 审核通过后, 球员是否确认入队(只有在球队申请了多个球队时,才会要求球员手动地确认)
    private Integer auditStatus; // 代表队长captain 对该入队申请的审核 auditStatusList

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }
}
