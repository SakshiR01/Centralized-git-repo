package com.gemini.Contripoint.model;

public class EnrolledEmail {
    private String employeeName;
    private String eventName;

    public EnrolledEmail(String employeeName, String eventName) {
        this.employeeName = employeeName;
        this.eventName = eventName;
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
}
