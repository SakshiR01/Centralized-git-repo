package com.gemini.Contripoint.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NotificationMessage {

    private ArrayList<String> notification;
    private String notificationMessage;
    private Integer read;
    private String status;
    private String type;
    private String date;
    private String time;


    public NotificationMessage(ArrayList<String> notification) {
        this.notification = notification;
    }

    public NotificationMessage(String notificationMessage, Integer read, String status, String type, String date, String time) {
        this.notificationMessage = notificationMessage;
        this.read = read;
        this.status = status;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getNotification() {
        return notification;
    }

    public void setNotification(ArrayList<String> notification) {
        this.notification = notification;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public Integer getRead() {
        return read;
    }

    public void setRead(Integer read) {
        this.read = read;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
