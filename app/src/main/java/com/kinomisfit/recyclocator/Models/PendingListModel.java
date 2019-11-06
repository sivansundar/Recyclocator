package com.kinomisfit.recyclocator.Models;

public class PendingListModel {

    String title;
    String id;
    String type;
    String timestamp;
    String quantity;
    String status;
    String imgUrl;


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

    public PendingListModel(String title, String id, String type, String timestamp, String quantity, String status, String imgUrl) {
        this.title = title;
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.quantity = quantity;
        this.status = status;
        this.imgUrl = imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
