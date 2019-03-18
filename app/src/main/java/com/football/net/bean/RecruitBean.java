package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class RecruitBean extends MineBean implements Serializable{
    private String content;
    private String title;
    private int id;
    private int isEnabled;
    private int isPublic;
    private long opTime;
    private TeamBean team;
    private Integer confirmStatus; // 确认状态
    private UserBean player;

    public UserBean getPlayer() {
        return player;
    }

    public void setPlayer(UserBean player) {
        this.player = player;
    }

    public Integer getConfirmStatus() {
        return confirmStatus;
    }

    public void setConfirmStatus(Integer confirmStatus) {
        this.confirmStatus = confirmStatus;
    }

    public String getContent() {
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

    public int getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(int isEnabled) {
        this.isEnabled = isEnabled;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public long getOpTime() {
        return opTime;
    }

    public void setOpTime(long opTime) {
        this.opTime = opTime;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }
}
