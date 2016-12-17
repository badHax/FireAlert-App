package com.example.keniel.test;

import java.util.Date;

/**
 * Created by Keniel on 11/29/2016.
 */

public class ForumItems {


    private String title;
    private String content;
    private String date;
    private Double lat;
    private Double lng;
    private String type;



    public ForumItems(String title, String content,String type, String date, Double lat, Double lng) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.lat = lat;
        this.lng = lng;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }
}
