package com.example.micraapp;

public class SliderData {
    private String title;
    private String description;
    private String image;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImageUrl(String imageUrl) {
        this.image= imageUrl;
    }

    public SliderData( String title, String image, String description) {
        this.title = title;
        this.description = description;
        this.image= image;
    }
}
