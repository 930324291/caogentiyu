package com.football.net.http.reponse.impl;

import com.football.net.bean.TeamBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class TeamBean2Result extends ListResult {

    TeamBean data;

    public TeamBean getData() {
        return data;
    }

    public void setData(TeamBean data) {
        this.data = data;
    }
}
