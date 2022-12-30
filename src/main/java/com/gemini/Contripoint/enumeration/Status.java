package com.gemini.Contripoint.enumeration;

public enum Status {
    DRAFT("DRAFTED"),
    PENDING("PENDING APPROVAL"),
    APPROVED("APPROVED"),
    DECLINED("DECLINED"),
    UPCOMING("UPCOMING"),
    ONGOING("ONGOING"),
    CLOSED("CLOSED");

    private final String name;

    Status(String statusName) {
        this.name = statusName;
    }

    public String getName() {
        return name;
    }

}