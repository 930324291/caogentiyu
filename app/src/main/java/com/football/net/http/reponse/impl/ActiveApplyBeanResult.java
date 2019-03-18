package com.football.net.http.reponse.impl;

import com.football.net.bean.ActiveApplyBean;
import com.football.net.http.reponse.Result;

/**
 * Created by footman on 2017/2/21.
 */

public class ActiveApplyBeanResult extends Result {

    ActiveApplyBean data;

    public ActiveApplyBean getData() {
        return data;
    }

    public void setData(ActiveApplyBean data) {
        this.data = data;
    }
}
