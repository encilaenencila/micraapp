package com.example.micraapp;

public class wastelistreport {
    String reportnumer;
    String dateReported;
    String location;
    String category;
    String Description;
    String photo;
    String resolvefeedback;
    String status;
    String categoryDescription;
    String resolvedate;
    String resolvetime;
    String timeReport;
    String repondate;
    String respondtime;
    String respondername;
    String respondmessag;
    String landmark;
    String finishproof;
    String residentfeedback;
    public String getDateReported() {
        return dateReported;
    }

    public String getLocation() {
        return location;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return Description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getResolvefeedback() {
        return resolvefeedback;
    }

    public String getStatus() {
        return status;
    }

    public String getCategoryDescription() {
        return categoryDescription;
    }

    public String getResolvedate() {
        return resolvedate;
    }

    public String getResolvetime() {
        return resolvetime;
    }

    public String getTimeReport() {
        return timeReport;
    }

    public String getRepondate() {
        return repondate;
    }

    public String getRespondtime() {
        return respondtime;
    }

    public String getRespondername() {
        return respondername;
    }

    public String getRespondmessag() {
        return respondmessag;
    }
    public String getLandmark() {
        return landmark;
    }

    public String getFinishproof() {
        return finishproof;
    }


    public String getResidentfeedback() {
        return residentfeedback;
    }


    public String getReportnumer() {
        return reportnumer;
    }

    public wastelistreport(String dateReported, String location, String category, String description, String photo, String resolvefeedback, String status, String categoryDescription, String resolvedate, String resolvetime, String timeReport, String repondate, String respondtime, String respondername, String respondmessag, String landmark,String finishproof,String residentfeedback,String reportnumer) {
        this.dateReported = dateReported;
        this.location = location;
        this.category = category;
        this.Description = description;
        this.photo = photo;
        this.resolvefeedback = resolvefeedback;
        this.status = status;
        this.categoryDescription = categoryDescription;
        this.resolvedate = resolvedate;
        this.resolvetime = resolvetime;
        this.timeReport = timeReport;
        this.repondate = repondate;
        this.respondtime = respondtime;
        this.respondername = respondername;
        this.respondmessag = respondmessag;
        this.landmark = landmark;
        this.finishproof = finishproof;
        this.residentfeedback = residentfeedback;
        this.reportnumer = reportnumer;
    }
}
