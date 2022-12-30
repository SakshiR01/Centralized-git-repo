package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gemini.Contripoint.enumeration.Active;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "contributions")
public class Contributions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int count;
    private int totalPoints;
    private String status;
    @Column(length = 400)
    private String description;
    private Date contributionDate;
    private String createdBy;
    private String lastModifiedBy;
    private String comments;
    private String summary;
    private String createdOn;
    private String lastModifiedOn;
    @Column(nullable = true)
    private int available;
    @Column(nullable = true)
    private int utilized;
    @Column(nullable = true)
    private Active active;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private ContributionCategory contributionCategory;
    @ManyToOne(fetch = FetchType.EAGER)   // foreign key defined to Employee table.
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee empId;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RelatedEmployee> relatedEmployee;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private AttachmentId attachmentId;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Project project;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ClientFeedback clientFeedback;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Notification notification;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Interview interview;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Certificate certificate;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private SelfDevelopment selfDevelopment;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private TrainingAndSession trainingAndSession;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TeamBuilding teamBuilding;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Event event;

    public TeamBuilding getTeamBuilding() {
        return teamBuilding;
    }

    public void setTeamBuilding(TeamBuilding teamBuilding) {
        this.teamBuilding = teamBuilding;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getContributionDate() {
        return contributionDate;
    }

    public void setContributionDate(Date contributionDate) {
        this.contributionDate = contributionDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public ContributionCategory getContributionCategory() {
        return contributionCategory;
    }

    public void setContributionCategory(ContributionCategory contributionCategory) {
        this.contributionCategory = contributionCategory;
    }

    public Employee getEmpId() {
        return empId;
    }

    public void setEmpId(Employee empId) {
        this.empId = empId;
    }

    public List<RelatedEmployee> getRelatedEmployee() {
        return relatedEmployee;
    }

    public void setRelatedEmployee(List<RelatedEmployee> relatedEmployee) {
        this.relatedEmployee = relatedEmployee;
    }

    public AttachmentId getAttachmentId() {
        return attachmentId;
    }

    public void setAttachmentId(AttachmentId attachmentId) {
        this.attachmentId = attachmentId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public ClientFeedback getClientFeedback() {
        return clientFeedback;
    }

    public void setClientFeedback(ClientFeedback clientFeedback) {
        this.clientFeedback = clientFeedback;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    public Interview getInterview() {
        return interview;
    }

    public void setInterview(Interview interview) {
        this.interview = interview;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public SelfDevelopment getSelfDevelopment() {
        return selfDevelopment;
    }

    public void setSelfDevelopment(SelfDevelopment selfDevelopment) {
        this.selfDevelopment = selfDevelopment;
    }

    public TrainingAndSession getTrainingAndSession() {
        return trainingAndSession;
    }

    public void setTrainingAndSession(TrainingAndSession trainingAndSession) {
        this.trainingAndSession = trainingAndSession;
    }

    public Active getActive() {
        return active;
    }

    public void setActive(Active active) {
        this.active = active;
    }

    public int getAvailable() {
        return available;
    }

    public void setAvailable(int available) {
        this.available = available;
    }

    public int getUtilized() {
        return utilized;
    }

    public void setUtilized(int utilized) {
        this.utilized = utilized;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }
}