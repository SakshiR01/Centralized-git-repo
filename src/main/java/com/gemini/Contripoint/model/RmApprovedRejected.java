package com.gemini.Contripoint.model;

public class RmApprovedRejected {
    private String adminname;
    private String eventName;

    public RmApprovedRejected(String adminname, String eventName) {
        this.adminname = adminname;
        this.eventName = eventName;
    }

    public String getAdminname() {
        return adminname;
    }

    public void setAdminname(String adminname) {
        this.adminname = adminname;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
