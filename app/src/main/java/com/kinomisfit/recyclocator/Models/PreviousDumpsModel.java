package com.kinomisfit.recyclocator.Models;

public class PreviousDumpsModel {

    String id;
    String imgurl;
    String timestamp;
    String title;
    String total;
    String type;

    public PreviousDumpsModel() {

    }


    public PreviousDumpsModel(String id, String imgurl, String timestamp, String title, String total, String type) {
        this.id = id;
        this.imgurl = imgurl;
        this.timestamp = timestamp;
        this.title = title;
        this.total = total;
        this.type = type;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String   getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
