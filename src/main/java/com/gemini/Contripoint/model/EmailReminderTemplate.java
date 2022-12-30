package com.gemini.Contripoint.model;

public class EmailReminderTemplate {

    private String EventName;
    private String EmployeeName;

    public EmailReminderTemplate(String eventName, String employeeName) {
        EventName = eventName;
        EmployeeName = employeeName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }
}
