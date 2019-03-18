package com.football.net.bean;

import com.football.net.common.util.StringUtils;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/21.
 */

public class TeamBean extends SearchBean implements Serializable {
    private String coverUrl;
    private String iconUrl;
    private String teamTitle;
    private String teamType;
    private int total;
    private int id;
    private int win;
    private int lost;
    private int even; //平
    private String region;
    //等级
    private int point;
    //人气
    private int likeNumLastWeek;
    //身价
    private int value;
    private int kind;
    private TeamMapBean map;
    private String introduce;
    private int likeNum;
    private long registTime;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public long getRegistTime() {
        return registTime;
    }

    public void setRegistTime(long registTime) {
        this.registTime = registTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public TeamMapBean getMap() {
        return map;
    }

    public void setMap(TeamMapBean map) {
        this.map = map;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getLikeNumLastWeek() {
        return likeNumLastWeek;
    }

    public void setLikeNumLastWeek(int likeNumLastWeek) {
        this.likeNumLastWeek = likeNumLastWeek;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTeamTitle() {
        if (StringUtils.isEmpty(teamTitle)) {
            return "";
        }
        return teamTitle;
    }

    public void setTeamTitle(String teamTitle) {
        this.teamTitle = teamTitle;
    }

    public String getTeamType() {
        return teamType;
    }

    public void setTeamType(String teamType) {
        this.teamType = teamType;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getEven() {
        return even;
    }

    public void setEven(int even) {
        this.even = even;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }
}
