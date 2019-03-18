package com.football.net.http.reponse.impl;

import com.football.net.bean.UserBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class UserBeanResult extends ListResult {

    ArrayList<UserBean> list;

    public ArrayList<UserBean> getList() {
        return list;
    }

    public void setList(ArrayList<UserBean> list) {
        this.list = list;
    }
}
