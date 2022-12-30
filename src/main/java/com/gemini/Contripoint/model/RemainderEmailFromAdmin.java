package com.gemini.Contripoint.model;

public class RemainderEmailFromAdmin {

    private String employeeName;
    private String eventName;
    private String eventDescription;
    private String startDate;

    public RemainderEmailFromAdmin(String employeeName, String eventName, String eventDescription, String startDate) {
        this.employeeName = employeeName;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
        this.startDate = startDate;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
}
