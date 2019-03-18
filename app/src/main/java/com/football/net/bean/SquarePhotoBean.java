package com.football.net.bean;

import android.text.TextUtils;

import com.football.net.common.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by footman on 2017/2/22.
 */

public class SquarePhotoBean extends MineBean implements Serializable {
    private String comment;
    private int id;
    private ArrayList<SquarePicBean> pics;
    private ArrayList<SquarePicBean> picsList;
    private long createTime;
    private String likes;
    private String viewTimes;


    public int getLikseNum() {
        if (TextUtils.isEmpty(likes)) {
            return 0;
        } else {
            return likes.split(",").length;
        }
    }


    public String getViewTimes() {
        if (StringUtils.isEmpty(viewTimes)) {
            viewTimes = "0";
        }
        return viewTimes;
    }

    public void setViewTimes(String viewTimes) {
        this.viewTimes = viewTimes;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<SquarePicBean> getPics() {
        return pics;
    }

    public void setPics(ArrayList<SquarePicBean> pics) {
        this.pics = pics;
    }

    public ArrayList<SquarePicBean> getPicsList() {
        return picsList;
    }

    public void setPicsList(ArrayList<SquarePicBean> picsList) {
        this.picsList = picsList;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "SquarePhotoBean{" +
                "viewTimes='" + viewTimes + '\'' +
                '}';
    }
}
