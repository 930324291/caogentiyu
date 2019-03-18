package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by 19062 on 2018/3/4.
 */

public class PlayerLikeBean implements Serializable {
    private Long id;
    private UserBean player; //被点赞的球员
    private UserBean giver;	//点赞者
    private Integer status;	//  状态   默认是1 ,暂时无取消功能

    public PlayerLikeBean(UserBean player, UserBean giver, Integer status) {
        this.player = player;
        this.giver = giver;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserBean getPlayer() {
        return player;
    }

    public void setPlayer(UserBean player) {
        this.player = player;
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
