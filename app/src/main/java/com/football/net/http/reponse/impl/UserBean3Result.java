package com.football.net.http.reponse.impl;

import com.football.net.bean.UserBean;
import com.football.net.http.reponse.Result;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class UserBean3Result extends Result {

    ArrayList<UserBean> data;

    public ArrayList<UserBean> getData() {
        return data;
    }

    public void setData(ArrayList<UserBean> data) {
        this.data = data;
    }
}
