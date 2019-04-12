package com.kinomisfit.recyclocator.Models;

public class PendingListModel {

    String title;
    String imgurl;
    String id;
    String timestamp;

    public PendingListModel() {}

    public PendingListModel(String title, String imgurl, String id, String timestamp) {
        this.title = title;
        this.timestamp = timestamp;
        this.imgurl = imgurl;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
