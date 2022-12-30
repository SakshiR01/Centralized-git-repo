package com.gemini.Contripoint.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EventContributionCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String contributionCategoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContributionCategoryId() {
        return contributionCategoryId;
    }

    public void setContributionCategoryId(String contributionCategoryId) {
        this.contributionCategoryId = contributionCategoryId;
    }
}
