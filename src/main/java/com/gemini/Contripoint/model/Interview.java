package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "interview")
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String month;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Profile> profile;     // Need to create a new table with one-to-many relation
    private String experienceLevel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<Profile> getProfile() {
        return profile;
    }

    public void setProfile(List<Profile> profile) {
        this.profile = profile;
    }

    public String getExperienceLevel() {
        return experienceLevel;
    }

    public void setExperienceLevel(String experienceLevel) {
        this.experienceLevel = experienceLevel;
    }
}
