package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "training_and_session")
public class TrainingAndSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Technology> technology;  // New table technology with one-to-many relationship
    private int headcount;
    @Column(nullable = true)
    private String otherTechnology;

    public String getOtherTechnology() {
        return otherTechnology;
    }

    public void setOtherTechnology(String otherTechnology) {
        this.otherTechnology = otherTechnology;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Technology> getTechnology() {
        return technology;
    }

    public void setTechnology(List<Technology> technology) {
        this.technology = technology;
    }
}
