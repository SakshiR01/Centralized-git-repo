package com.gemini.Contripoint.model;

import com.gemini.Contripoint.enumeration.NotificationStatus;

import javax.persistence.*;

@Entity
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String notificationMessage;
    private String contributionCategory;
    private NotificationStatus notificationStatus;
    private String empId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getContributionCategory() {
        return contributionCategory;
    }

    public void setContributionCategory(String contributionCategory) {
        this.contributionCategory = contributionCategory;
    }

    public NotificationStatus getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(NotificationStatus notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
