package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "Enrolled")
public class Enrolled {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isEnrolled;
    @Column(nullable = true)
    private String contributionCategory;
    private String employee;
    private StringBuilder certificateURL;
    private String certificateName;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Entry entry;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isEnrolled() {
        return isEnrolled;
    }

    public void setEnrolled(boolean enrolled) {
        isEnrolled = enrolled;
    }

    public String getContributionCategory() {
        return contributionCategory;
    }

    public void setContributionCategory(String contributionCategory) {
        this.contributionCategory = contributionCategory;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public StringBuilder getCertificateURL() {
        return certificateURL;
    }

    public void setCertificateURL(StringBuilder certificateURL) {
        this.certificateURL = certificateURL;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
