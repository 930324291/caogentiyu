package com.football.net.bean;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class SquarePicBean implements Serializable {
    private String url;
    private int id;
    private int status;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
