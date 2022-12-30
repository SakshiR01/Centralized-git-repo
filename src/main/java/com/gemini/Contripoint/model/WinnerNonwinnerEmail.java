package com.gemini.Contripoint.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class WinnerNonwinnerEmail {

    private String employeeName;
    private String eventName;
    private String rewards;
    private AtomicReference<String> code;
    private ArrayList<HashMap> listOfRankers;

    public WinnerNonwinnerEmail(String employeeName, String eventName, String rewards, AtomicReference<String> code) {
        this.employeeName = employeeName;
        this.eventName = eventName;
        this.rewards = rewards;
        this.code = code;
    }

    public WinnerNonwinnerEmail(String employeeName, String eventName, String rewards) {
        this.employeeName = employeeName;
        this.eventName = eventName;
        this.rewards = rewards;
    }

    public WinnerNonwinnerEmail(String employeeName, String eventName, ArrayList listOfRankers) {
        this.employeeName = employeeName;
        this.eventName = eventName;
        this.listOfRankers = listOfRankers;
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

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public AtomicReference<String> getCode() {
        return code;
    }

    public void setCode(AtomicReference<String> code) {
        this.code = code;
    }

    public ArrayList<HashMap> getListOfRankers() {
        return listOfRankers;
    }

    public void setListOfRankers(ArrayList<HashMap> listOfRankers) {
        this.listOfRankers = listOfRankers;
    }
}
