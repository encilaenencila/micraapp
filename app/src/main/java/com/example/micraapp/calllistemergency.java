package com.example.micraapp;

public class calllistemergency {
    String department;
    String hotline;
    int iconimage;
    public int getIconimage() {
        return iconimage;
    }

    String backgroundcolor;

    public String getDepartment() {
        return department;
    }
    public String getBackgroundcolor() {
        return backgroundcolor;
    }
    public void setBackgroundcolor(String backgroundcolor) {
        this.backgroundcolor = backgroundcolor;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
    public String getHotline() {
        return hotline;
    }
    public void setHotline(String hotline) {
        this.hotline = hotline;
    }
    public calllistemergency(String department, String hotline,String color,int image) {
        this.department = department;
        this.hotline = hotline;
        this.backgroundcolor = color;
        this.iconimage = image;
    }
}
