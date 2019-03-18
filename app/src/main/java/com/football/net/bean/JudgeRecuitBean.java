package com.football.net.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class JudgeRecuitBean extends MineBean implements Serializable{
    private int id;
    private TeamBean team;

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
