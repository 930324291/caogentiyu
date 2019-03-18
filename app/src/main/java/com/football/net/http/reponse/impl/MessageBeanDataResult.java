package com.football.net.http.reponse.impl;

import com.football.net.bean.MessageBean;
import com.football.net.http.reponse.ListResult;
import com.football.net.http.reponse.Result;

import java.util.ArrayList;

/**
 * Created by footman on 2017/2/21.
 */

public class MessageBeanDataResult extends Result {

    MessageBean data;

    public MessageBean getData() {
        return data;
    }

    public void setData(MessageBean data) {
        this.data = data;
    }
}
