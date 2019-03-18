package com.football.net.bean;

/**
 * Created by footman on 2017/2/21.
 */

public class TeamMemberBean {
    private String name;
    private String iconUrl;
    private String birth ;
    private int position ;
    private String idno;
    private TeamBean team;
    private int height;
    private int likeNumLastWeek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public int getLikeNumLastWeek() {
        return likeNumLastWeek;
    }

    public void setLikeNumLastWeek(int likeNumLastWeek) {
        this.likeNumLastWeek = likeNumLastWeek;
    }
}
