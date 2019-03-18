package com.football.net.http.reponse.impl;

import com.football.net.bean.SquareVideoBean;
import com.football.net.http.reponse.ListResult;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class SquareVideo2BeanResult extends ListResult {

    SquareVideoBean data;

    public SquareVideoBean getData() {
        return data;
    }

    public void setData(SquareVideoBean data) {
        this.data = data;
    }
}
