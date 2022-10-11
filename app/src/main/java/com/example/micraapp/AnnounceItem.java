package com.example.micraapp;


public class AnnounceItem {
    private String id;
    private String title;
    private String date;
    private String image;
    private String description;
    private String postedby;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getPostedby() {
        return postedby;
    }

    public AnnounceItem(String id, String title, String date, String image, String description, String postedby) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.image = image;
        this.description = description;
        this.postedby = postedby;
    }
}
