package com.gemini.Contripoint.model;

import java.util.List;

public class EnrolledNotEnrolledResponse {

    List<Employee> participants;
    List<Employee> enrolled;
    List<Employee> notEnrolled;

    public EnrolledNotEnrolledResponse(List<Employee> participants) {
        this.participants = participants;
    }

    public EnrolledNotEnrolledResponse(List<Employee> enrolled, List<Employee> notEnrolled) {
        this.enrolled = enrolled;
        this.notEnrolled = notEnrolled;
    }

    public List<Employee> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Employee> participants) {
        this.participants = participants;
    }

    public List<Employee> getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(List<Employee> enrolled) {
        this.enrolled = enrolled;
    }

    public List<Employee> getNotEnrolled() {
        return notEnrolled;
    }

    public void setNotEnrolled(List<Employee> notEnrolled) {
        this.notEnrolled = notEnrolled;
    }
}
