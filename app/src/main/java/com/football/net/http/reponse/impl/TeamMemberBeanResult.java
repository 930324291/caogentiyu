package com.football.net.http.reponse.impl;

import com.football.net.bean.TeamBean;
import com.football.net.bean.TeamMemberBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class TeamMemberBeanResult extends ListResult {

    ArrayList<TeamMemberBean> list;

    public ArrayList<TeamMemberBean> getList() {
        return list;
    }

    public void setList(ArrayList<TeamMemberBean> list) {
        this.list = list;
    }
}
