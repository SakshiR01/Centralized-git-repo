package com.gemini.Contripoint.model;

public class EventInvitationTemplate {

    private String EmployeeName;
    private String EventName;
    private String EndDate;
    private String Discription;

    private String DateFormate;

    public <dateFormate> EventInvitationTemplate(String employeeName, String eventName, String endDate, String discription, String dateFormate) {
        EmployeeName = employeeName;
        EventName = eventName;
        EndDate = endDate;
        Discription = discription;

        DateFormate = dateFormate;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getDiscription() {
        return Discription;
    }

    public void setDiscription(String discription) {
        Discription = discription;
    }


    public String getDateFormate() {
        return DateFormate;
    }

    public void setDateFormate(String dateFormate) {
        DateFormate = dateFormate;
    }
}
