package com.football.net.http.reponse.impl;

import com.football.net.bean.SquarePhotoBean;
import com.football.net.bean.TeamMemberBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class SquarePhotoBeanResult extends ListResult {

    ArrayList<SquarePhotoBean> list;

    public ArrayList<SquarePhotoBean> getList() {
        return list;
    }

    public void setList(ArrayList<SquarePhotoBean> list) {
        this.list = list;
    }
}
