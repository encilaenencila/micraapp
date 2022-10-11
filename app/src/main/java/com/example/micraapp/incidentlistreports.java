package com.example.micraapp;

public class incidentlistreports {
    String resolveStatus;
    String category;
    String crimecategory;
    String evidence;
    String locationofincident;
    String casenarrative;
    String respondedBy;
    String respondDate;
    String respondTime;
    String respondMessage;
    String landmark;
    String resolvefeedback;
    String residentfeedback;
    String reportid;
    String finishproof;
    String reportDate;



    public String getResolveStatus() {
        return resolveStatus;
    }

    public String getCategory() {
        return category;
    }

    public String getCrimecategory() {
        return crimecategory;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getLocationofincident() {
        return locationofincident;
    }

    public String getCasenarrative() {
        return casenarrative;
    }

    public String getRespondedBy() {
        return respondedBy;
    }

    public String getRespondDate() {
        return respondDate;
    }

    public String getRespondTime() {
        return respondTime;
    }

    public String getRespondMessage() {
        return respondMessage;
    }

    public String getLandmark() {
        return landmark;
    }
    public String getResolvefeedback() {
        return resolvefeedback;
    }

    public String getResidentfeedback() {
        return residentfeedback;
    }


    public String getReportid() {
        return reportid;
    }

    public String getFinishproof() {
        return finishproof;
    }

    public String getReportDate() {
        return reportDate;
    }
    public incidentlistreports(String resolveStatus, String category, String crimecategory, String evidence, String locationofincident, String casenarrative, String respondedBy, String respondDate, String respondTime, String respondMessage, String landmark, String resolvefeedback, String residentfeedback,String reportid,String finishproof,String reportDate) {
        this.resolveStatus = resolveStatus;
        this.category = category;
        this.crimecategory = crimecategory;
        this.evidence = evidence;
        this.locationofincident = locationofincident;
        this.casenarrative = casenarrative;
        this.respondedBy = respondedBy;
        this.respondDate = respondDate;
        this.respondTime = respondTime;
        this.respondMessage = respondMessage;
        this.landmark = landmark;
        this.resolvefeedback = resolvefeedback;
        this.residentfeedback = residentfeedback;
        this.reportid = reportid;
        this.finishproof = finishproof;
        this.reportDate = reportDate;
    }
}
