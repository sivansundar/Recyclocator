package com.kinomisfit.recyclocator.Models;

public class PendingListModel {

    String title;
    String imgurl;
    String id;
    String type;
    String timestamp;
    String quantity;
    String status;

    public PendingListModel() {}

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public PendingListModel(String title, String imgurl, String id, String timestamp, String quantity, String status, String type) {
        this.title = title;
        this.timestamp = timestamp;
        this.imgurl = imgurl;
        this.type = type;
        this.id = id;
        this.quantity = quantity;
        this.status = status;

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
