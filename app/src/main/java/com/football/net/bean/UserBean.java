package com.football.net.bean;

import com.football.net.common.util.StringUtils;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class UserBean extends SearchBean implements Serializable {
    private String id = "";
    private String name = "";
    private String nickname= "";
    private TeamBean team = new TeamBean();
    private long birth;
    private long verifyTime;
    private int auditStatus = 1;
    //球员位置
    private String coordinate;
    private int x = -1;
    private int y = -1;

    private int isCaptain; //1是队长，2是队员
    private String iconUrl;
    private String height;
    private String weight;
    private String position;
    private int likeNum;

    private int likeSeq; // 人气排名

    //人气
    private int likeNumLastWeek;
    //身价
    private int value;
    //等级
    private int point;
    private int gender;
    private int official;
    private String mobile;
    //场上号码
    private String uniformNumber;

    private String qq;
    private String wechat;
    private long createTime;
    //平
    private int even;
    //失败总次数
    private int lost;
    //赢
    private int win;
    //总场数
    private int total;
    //出勤次数
    private String attendTimes;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getAttendTimes() {
        return attendTimes;
    }

    public void setAttendTimes(String attendTimes) {
        this.attendTimes = attendTimes;
    }

    public int getEven() {
        return even;
    }

    public void setEven(int even) {
        this.even = even;
    }

    public int getLost() {
        return lost;
    }

    public void setLost(int lost) {
        this.lost = lost;
    }

    public int getWin() {
        return win;
    }

    public void setWin(int win) {
        this.win = win;
    }

    public int getLikeNumLastWeek() {
        return likeNumLastWeek;
    }

    public void setLikeNumLastWeek(int likeNumLastWeek) {
        this.likeNumLastWeek = likeNumLastWeek;
    }

    public int getLocationx() {
        return x;
    }

    public void setLocationx(int locationx) {
        this.x = locationx;
    }

    public int getLocationy() {
        return y;
    }

    public void setLocationy(int locationy) {
        this.y = locationy;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getQq() {
        if (StringUtils.isEmpty(qq)) {
            qq = "";
        }
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getWechat() {
        if (StringUtils.isEmpty(wechat)) {
            wechat = "";
        }
        return wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(long verifyTime) {
        this.verifyTime = verifyTime;
    }

    public int getOfficial() {
        return official;
    }

    public void setOfficial(int official) {
        this.official = official;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getId() {
        if (StringUtils.isEmpty(id)) {
            id = "";
        }
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        if (StringUtils.isEmpty(nickname)) {
            nickname = "";
        }
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public TeamBean getTeam() {
        return team;
    }

    public void setTeam(TeamBean team) {
        this.team = team;
    }

    public int getIsCaptain() {
        return isCaptain;
    }

    public void setIsCaptain(int isCaptain) {
        this.isCaptain = isCaptain;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getHeight() {
        if (StringUtils.isEmpty(height)) {
            height = "";
        }
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        if (StringUtils.isEmpty(weight)) {
            weight = "";
        }
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getLikeSeq() {
        return likeSeq;
    }

    public void setLikeSeq(int likeSeq) {
        this.likeSeq = likeSeq;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getUniformNumber() {
        if (StringUtils.isEmpty(uniformNumber)) {
            uniformNumber = "";
        }
        return uniformNumber;
    }

    public void setUniformNumber(String uniformNumber) {
        this.uniformNumber = uniformNumber;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
