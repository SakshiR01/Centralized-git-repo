package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private Date dateOfCompletion;
    @Column(nullable = true)
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Technology> technology;
    @Column(nullable = true)
    private String otherTechnology;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDateOfCompletion() {
        return dateOfCompletion;
    }

    public void setDateOfCompletion(Date dateOfCompletion) {
        this.dateOfCompletion = dateOfCompletion;
    }

    public List<Technology> getTechnology() {
        return technology;
    }

    public void setTechnology(List<Technology> technology) {
        this.technology = technology;
    }

    public String getOtherTechnology() {
        return otherTechnology;
    }

    public void setOtherTechnology(String otherTechnology) {
        this.otherTechnology = otherTechnology;
    }
}
