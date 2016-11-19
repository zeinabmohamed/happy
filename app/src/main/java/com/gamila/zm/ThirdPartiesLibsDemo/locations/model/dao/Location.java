package com.gamila.zm.ThirdPartiesLibsDemo.locations.model.dao;


import android.app.Activity;
import android.net.Uri;

import com.gamila.zm.ThirdPartiesLibsDemo.util.LocationUtil;

import java.io.Serializable;

/**
 * Created by zeinab on 4/30/2016.
 */
public class Location implements Serializable {

    private String city;
    private String lat;
    private String log;
    private String phone;
    private String mail;
    private String url;
    private String videoId = "";
    private double distance;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public android.location.Location getLocation() {

        android.location.Location targetLocation = new android.location.Location("");//provider name is unecessary
        targetLocation.setLatitude(Double.parseDouble(getLat()));//your coords of course
        targetLocation.setLongitude(Double.parseDouble(getLog()));

        return targetLocation;
    }

    public String getVideoId() {
        if (videoId.isEmpty()) {
            Uri uri = Uri.parse(getUrl());
            videoId = uri.getQueryParameter("v");
        }
        return videoId;
    }

    public double getDistance(Activity activity) {
        if(distance == 0) {
            distance = LocationUtil.getInstance().getDistanceInKm(activity,getLocation());
        }
        return distance;
    }
}
