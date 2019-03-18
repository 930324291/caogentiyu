package com.football.net.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by footman on 2017/2/22.
 */

public class MessageBean extends MineBean implements Serializable{

    private long beginTime;
    private long opTime;
    private String content;
    private String title;
    private String address;
    private int id;
    private int isEnabled;
    private int messageType;
    private TeamBean team;
    private List<TankInMessageBean> tanks;
    private List<TankInMessageBean> signTanks;

    public List<TankInMessageBean> getSignTanks() {
        return signTanks;
    }

    public void setSignTanks(List<TankInMessageBean> signTanks) {
        this.signTanks = signTanks;
    }

    public long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(long beginTime) {
        this.beginTime = beginTime;
    }

    public long getOpTime() {
        return opTime;
    }

    public void setOpTime(long opTime) {
        this.opTime = opTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public List<TankInMessageBean> getTanks() {
        return tanks;
    }

    public void setTanks(List<TankInMessageBean> tanks) {
        this.tanks = tanks;
    }
}
