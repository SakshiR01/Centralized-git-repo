package com.gemini.Contripoint.model;


public class PendingActivityApproval {
    private String rmName;
    private String eventName;
    private String startDate1;
    private String endDate1;
    private String eventDescription;

    public PendingActivityApproval(String rmName, String eventName, String startDate1, String endDate1, String eventDescription) {
        this.rmName = rmName;
        this.eventName = eventName;
        this.startDate1 = startDate1;
        this.endDate1 = endDate1;
        this.eventDescription = eventDescription;
    }

    public String getRmName() {
        return rmName;
    }

    public void setRmName(String rmName) {
        this.rmName = rmName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartDate1() {
        return startDate1;
    }

    public void setStartDate1(String startDate1) {
        this.startDate1 = startDate1;
    }

    public String getEndDate1() {
        return endDate1;
    }

    public void setEndDate1(String endDate1) {
        this.endDate1 = endDate1;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

}
