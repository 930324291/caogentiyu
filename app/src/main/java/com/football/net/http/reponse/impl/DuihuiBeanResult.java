package com.football.net.http.reponse.impl;

import com.football.net.bean.DuihuiBean;
import com.football.net.bean.RecruitBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class DuihuiBeanResult extends ListResult {

    ArrayList<DuihuiBean> data;

    public ArrayList<DuihuiBean> getData() {
        return data;
    }

    public void setData(ArrayList<DuihuiBean> data) {
        this.data = data;
    }
}
