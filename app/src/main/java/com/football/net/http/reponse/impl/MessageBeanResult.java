package com.football.net.http.reponse.impl;

import com.football.net.bean.MessageBean;
import com.football.net.bean.RecruitBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class MessageBeanResult extends ListResult {

    ArrayList<MessageBean> list;

    public ArrayList<MessageBean> getList() {
        return list;
    }

    public void setList(ArrayList<MessageBean> list) {
        this.list = list;
    }
}
