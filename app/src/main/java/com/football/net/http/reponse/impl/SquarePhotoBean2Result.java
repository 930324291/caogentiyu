package com.football.net.http.reponse.impl;

import com.football.net.bean.SquarePhotoBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class SquarePhotoBean2Result extends ListResult {

    SquarePhotoBean data;

    public SquarePhotoBean getData() {
        return data;
    }

    public void setData(SquarePhotoBean data) {
        this.data = data;
    }
}
