package com.football.net.http.reponse.impl;

import com.football.net.bean.ApplyBean;
import com.football.net.bean.MessageOutBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class MessageOutBeanResult extends ListResult {

    ArrayList<MessageOutBean> list;

    public ArrayList<MessageOutBean> getList() {
        return list;
    }

    public void setList(ArrayList<MessageOutBean> list) {
        this.list = list;
    }
}
