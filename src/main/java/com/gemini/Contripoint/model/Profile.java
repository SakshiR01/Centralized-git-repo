package com.gemini.Contripoint.model;

import javax.persistence.*;

@Entity
@Table
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String EmployeeProfile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeProfile() {
        return EmployeeProfile;
    }

    public void setEmployeeProfile(String employeeProfile) {
        EmployeeProfile = employeeProfile;
    }
}
