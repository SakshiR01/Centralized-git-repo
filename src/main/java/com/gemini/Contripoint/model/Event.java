package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "Event")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String adminEmployeeId;
    private String rewards;
    private int targetPoints;
    private String summary;
    private Date startDate;
    private Date endDate;
    private String Description;
    private String status;
    private String eventType;
    private String createdOn;
    private String lastModifiedOn;
    private String carousalFileName;
    private String bannerFileName;
    private StringBuilder carousalURL;
    private StringBuilder bannerURL;
    private String colorCode;
    @Column(nullable = true, columnDefinition = "boolean default false")
    private boolean uploadEntry;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<EventContributionCategory> eventContributionCategory;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Enrolled> enrolled;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Winner> winners;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminEmployeeId() {
        return adminEmployeeId;
    }

    public void setAdminEmployeeId(String adminEmployeeId) {
        this.adminEmployeeId = adminEmployeeId;
    }

    public String getRewards() {
        return rewards;
    }

    public void setRewards(String rewards) {
        this.rewards = rewards;
    }

    public int getTargetPoints() {
        return targetPoints;
    }

    public void setTargetPoints(int targetPoints) {
        this.targetPoints = targetPoints;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<EventContributionCategory> getContributionCategory() {
        return eventContributionCategory;
    }

    public void setContributionCategory(List<EventContributionCategory> contributionCategory) {
        this.eventContributionCategory = contributionCategory;
    }

    public List<EventContributionCategory> getEventContributionCategory() {
        return eventContributionCategory;
    }

    public void setEventContributionCategory(List<EventContributionCategory> eventContributionCategory) {
        this.eventContributionCategory = eventContributionCategory;
    }

    public List<Enrolled> getEnrolled() {
        return enrolled;
    }

    public void setEnrolled(List<Enrolled> enrolled) {
        this.enrolled = enrolled;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(String lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public List<Winner> getWinners() {
        return winners;
    }

    public void setWinners(List<Winner> winners) {
        this.winners = winners;
    }

    public String getCarousalFileName() {
        return carousalFileName;
    }

    public void setCarousalFileName(String carousalFileName) {
        this.carousalFileName = carousalFileName;
    }

    public String getBannerFileName() {
        return bannerFileName;
    }

    public void setBannerFileName(String bannerFileName) {
        this.bannerFileName = bannerFileName;
    }

    public StringBuilder getCarousalURL() {
        return carousalURL;
    }

    public void setCarousalURL(StringBuilder carousalURL) {
        this.carousalURL = carousalURL;
    }

    public StringBuilder getBannerURL() {
        return bannerURL;
    }

    public void setBannerURL(StringBuilder bannerURL) {
        this.bannerURL = bannerURL;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public boolean isUploadEntry() {
        return uploadEntry;
    }

    public void setUploadEntry(boolean uploadEntry) {
        this.uploadEntry = uploadEntry;
    }
}
