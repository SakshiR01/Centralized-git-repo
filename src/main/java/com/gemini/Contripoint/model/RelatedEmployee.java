package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "related_employee")
public class RelatedEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String empId;
    private int duration;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Technology> technology;  // Linked with technology table with one-to-many relationship

    @Column(nullable = true)
    private String otherTechnology;

    public String getOtherTechnology() {
        return otherTechnology;
    }

    public void setOtherTechnology(String otherTechnology) {
        this.otherTechnology = otherTechnology;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public List<Technology> getTechnology() {
        return technology;
    }

    public void setTechnology(List<Technology> technology) {
        this.technology = technology;
    }
}
