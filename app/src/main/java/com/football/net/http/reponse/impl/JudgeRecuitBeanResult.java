package com.football.net.http.reponse.impl;

import com.football.net.bean.JudgeRecuitBean;
import com.football.net.bean.SquareVideoBean;
import com.football.net.http.reponse.ListResult;
import com.football.net.http.reponse.Result;

/**
 * Created by footman on 2017/2/21.
 */

public class JudgeRecuitBeanResult extends Result {

    JudgeRecuitBean data;

    public JudgeRecuitBean getData() {
        return data;
    }

    public void setData(JudgeRecuitBean data) {
        this.data = data;
    }
}
