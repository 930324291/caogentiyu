package com.football.net.http.reponse.impl;

import com.football.net.bean.AdsPhotoBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.http.reponse.ListResult;
import com.football.net.http.reponse.Result;

import java.util.List;

/**
 * Created by footman on 2017/2/21.
 */

public class AdsPhotoBeanListResult extends Result {

    List<AdsPhotoBean> data;

    public List<AdsPhotoBean> getData() {
        return data;
    }

    public void setData(List<AdsPhotoBean> data) {
        this.data = data;
    }
}
