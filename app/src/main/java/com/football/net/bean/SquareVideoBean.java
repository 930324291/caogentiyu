package com.football.net.bean;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class SquareVideoBean extends MineBean implements Serializable{
    private String title;
    private String comment;
    private String screenshot;
    private String videoDiv;
    private String likes;
    private int commentCount;
    private int viewTimes;
    private int id;
    private UserBean player;
    private long createTime;

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getViewTimes() {
        return viewTimes;
    }

    public void setViewTimes(int viewTimes) {
        this.viewTimes = viewTimes;
    }

    public int getLikseNum(){
        if(TextUtils.isEmpty(likes)){
            return 0;
        }else{
            return  likes.split(",").length;
        }
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

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }

    public String getVideoDiv() {
        return videoDiv;
    }

    public void setVideoDiv(String videoDiv) {
        this.videoDiv = videoDiv;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public UserBean getPlayer() {
        return player;
    }

    public void setPlayer(UserBean player) {
        this.player = player;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    @Override
    public String toString() {
        return "SquareVideoBean{" +
                "commentCount=" + commentCount +
                '}';
    }
}
