package com.football.net.http.reponse.impl;

import com.football.net.bean.ApplyBean;
import com.football.net.bean.ApplyBean2;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class ApplyBean2Result extends ListResult {

    ArrayList<ApplyBean2> list;

    public ArrayList<ApplyBean2> getList() {
        return list;
    }

    public void setList(ArrayList<ApplyBean2> list) {
        this.list = list;
    }
}
