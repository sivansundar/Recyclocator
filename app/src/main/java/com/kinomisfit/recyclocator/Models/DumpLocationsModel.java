package com.kinomisfit.recyclocator.Models;

public class DumpLocationsModel {

    double latitude;
    double longitude;
    String id;
    String title;

    public DumpLocationsModel() {

    }

    public DumpLocationsModel(double latitude, double longitude, String id, String title) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
