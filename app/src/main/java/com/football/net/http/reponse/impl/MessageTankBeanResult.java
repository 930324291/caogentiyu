package com.football.net.http.reponse.impl;

import com.football.net.bean.MessageBean;
import com.football.net.bean.MessageTankBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class MessageTankBeanResult extends ListResult {

    ArrayList<MessageTankBean> list;

    public ArrayList<MessageTankBean> getList() {
        return list;
    }

    public void setList(ArrayList<MessageTankBean> list) {
        this.list = list;
    }
}
