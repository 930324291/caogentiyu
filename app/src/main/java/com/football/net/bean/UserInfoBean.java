package com.football.net.bean;

/**
 * Created by footman on 2017/5/10.
 */

public class UserInfoBean{
    int imageRid;
    String name;
    String cName;
    public UserInfoBean(int imageRid, String name) {
        this.imageRid = imageRid;
        this.name = name;
    }

    public UserInfoBean(int imageRid, String cName,String name) {
        this.imageRid = imageRid;
        this.cName = cName;
        this.name = name;
    }

    public String getcName() {
        return cName;
    }

    public int getImageRid() {
        return imageRid;
    }

    public void setImageRid(int imageRid) {
        this.imageRid = imageRid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
