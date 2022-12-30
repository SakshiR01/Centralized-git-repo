package com.gemini.Contripoint.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    private String id;
    private String name;
    private String designation;
    private String email;
    private String team;
    private byte[] image;
    private String rmId;
    @Column(columnDefinition = "boolean default false")
    private boolean isAdmin; //something strange here, won't work as 'int'

    public Employee() {
    }

    public Employee(String id, String name, String designation, byte[] image) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.image = image;
    }

    public Employee(String id, String name, String designation, String email, String team, byte[] image, String rmId) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.team = team;
        this.image = image;
        this.rmId = rmId;
    }

    public Employee(String id, String name, String designation, String email, String team, byte[] image, String rmId, boolean isAdmin) {
        this.id = id;
        this.name = name;
        this.designation = designation;
        this.email = email;
        this.team = team;
        this.image = image;
        this.rmId = rmId;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getRmId() {
        return rmId;
    }

    public void setRmId(String rmId) {
        this.rmId = rmId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
