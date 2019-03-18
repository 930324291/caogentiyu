package com.football.net.http.reponse.impl;

import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class SquareVideoBeanResult extends ListResult {

    ArrayList<SquareVideoBean> list;

    public ArrayList<SquareVideoBean> getList() {
        return list;
    }

    public void setList(ArrayList<SquareVideoBean> list) {
        this.list = list;
    }
}
