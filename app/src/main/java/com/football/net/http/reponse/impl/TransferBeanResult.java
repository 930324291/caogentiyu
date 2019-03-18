package com.football.net.http.reponse.impl;

import com.football.net.bean.RecruitBean;
import com.football.net.bean.TransferBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class TransferBeanResult extends ListResult {

    ArrayList<TransferBean> list;

    public ArrayList<TransferBean> getList() {
        return list;
    }

    public void setList(ArrayList<TransferBean> list) {
        this.list = list;
    }
}
