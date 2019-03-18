package com.football.net.bean;

/**
 * Created by footman on 2017/2/22.
 */

public class CommentBean {

    private long opTime;
    private String comment;
    private int id;
    private int itemId;
    private UserBean player;

    public long getOpTime() {
        return opTime;
    }

    public void setOpTime(long opTime) {
        this.opTime = opTime;
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

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public UserBean getPlayer() {
        return player;
    }

    public void setPlayer(UserBean player) {
        this.player = player;
    }
}
