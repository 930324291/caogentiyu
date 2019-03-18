package com.football.net.http.reponse.impl;

import com.football.net.bean.ApplyBean;
import com.football.net.bean.RecruitBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class ApplyBeanResult extends ListResult {

    ArrayList<ApplyBean> list;

    public ArrayList<ApplyBean> getList() {
        return list;
    }

    public void setList(ArrayList<ApplyBean> list) {
        this.list = list;
    }
}
