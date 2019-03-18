package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by 19062 on 2018/3/4.
 */

public class TeamLikeBean implements Serializable {

    private Long id;
    private TeamBean team; //被赞球队
    private UserBean giver;	//点赞者
    private Integer status;	// 默认1 目前没有取消点赞方法

    public TeamLikeBean(TeamBean team, UserBean giver, Integer status) {
        this.team = team;
        this.giver = giver;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public UserBean getGiver() {
        return giver;
    }

    public void setGiver(UserBean giver) {
        this.giver = giver;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
