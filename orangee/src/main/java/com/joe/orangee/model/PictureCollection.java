package com.joe.orangee.model;

/**
 * Created by qiaorongzhu on 2014/12/22.
 */
public class PictureCollection {

    private String url;
    private WeiboStatus status;

    public PictureCollection() {
    }

    public PictureCollection(String url, WeiboStatus status) {
        this.url = url;
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public WeiboStatus getStatus() {
        return status;
    }

    public void setStatus(WeiboStatus status) {
        this.status = status;
    }
}
