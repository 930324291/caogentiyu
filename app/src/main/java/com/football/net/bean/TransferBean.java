package com.football.net.bean;

/**
 * Created by footman on 2017/2/22.
 */

public class TransferBean {
    private long fromTime;
    private long toTime;
    private TeamBean fromTeam;
    private TeamBean toTeam;
    private UserBean player;

    public long getFromTime() {
        return fromTime;
    }

    public void setFromTime(long fromTime) {
        this.fromTime = fromTime;
    }

    public long getToTime() {
        return toTime;
    }

    public void setToTime(long toTime) {
        this.toTime = toTime;
    }

    public TeamBean getFromTeam() {
        return fromTeam;
    }

    public void setFromTeam(TeamBean fromTeam) {
        this.fromTeam = fromTeam;
    }

    public TeamBean getToTeam() {
        return toTeam;
    }

    public void setToTeam(TeamBean toTeam) {
        this.toTeam = toTeam;
    }

    public UserBean getPlayer() {
        return player;
    }

    public void setPlayer(UserBean player) {
        this.player = player;
    }
}
