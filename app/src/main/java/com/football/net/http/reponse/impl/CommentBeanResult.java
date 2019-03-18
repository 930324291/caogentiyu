package com.football.net.http.reponse.impl;

import com.football.net.bean.ApplyBean;
import com.football.net.bean.CommentBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class CommentBeanResult extends ListResult {

    ArrayList<CommentBean> list;

    public ArrayList<CommentBean> getList() {
        return list;
    }

    public void setList(ArrayList<CommentBean> list) {
        this.list = list;
    }
}
