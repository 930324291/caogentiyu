package com.football.net.http.reponse.impl;

import com.football.net.bean.ApplyBean;
import com.football.net.bean.FieldBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class FieldBeanResult extends ListResult {

    ArrayList<FieldBean> list;

    public ArrayList<FieldBean> getList() {
        return list;
    }

    public void setList(ArrayList<FieldBean> list) {
        this.list = list;
    }
}
