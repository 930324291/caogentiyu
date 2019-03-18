package com.football.net.http.reponse.impl;

import com.football.net.bean.RecruitBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class RecruitBeanResult extends ListResult {

    ArrayList<RecruitBean> list;

    public ArrayList<RecruitBean> getList() {
        return list;
    }

    public void setList(ArrayList<RecruitBean> list) {
        this.list = list;
    }
}
