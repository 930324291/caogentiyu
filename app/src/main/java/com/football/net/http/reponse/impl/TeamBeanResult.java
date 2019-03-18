package com.football.net.http.reponse.impl;

import com.football.net.bean.TeamBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class TeamBeanResult extends ListResult {

    ArrayList<TeamBean> list;

    public ArrayList<TeamBean> getList() {
        return list;
    }

    public void setList(ArrayList<TeamBean> list) {
        this.list = list;
    }
}
