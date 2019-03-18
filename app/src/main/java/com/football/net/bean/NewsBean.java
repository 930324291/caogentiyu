package com.football.net.bean;

import com.football.net.http.reponse.Result;

import java.io.Serializable;

/**
 * Created by footman on 2017/2/22.
 */

public class NewsBean extends Result implements Serializable{

    private long createTime;
    private long startTime;
    private String content;
    private String title;
    private int id;
    private int status;
    private int modifier;
    private int creater;
    private String thumbnail;

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    public int getCreater() {
        return creater;
    }

    public void setCreater(int creater) {
        this.creater = creater;
    }

    @Override
    public String toString() {
        return "NewsBean{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
