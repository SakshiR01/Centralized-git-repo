package com.gemini.Contripoint.model;

import javax.persistence.*;

@Entity
@Table
public class Technology {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String EmployeeTechnology;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmployeeTechnology() {
        return EmployeeTechnology;
    }

    public void setEmployeeTechnology(String employeeTechnology) {
        EmployeeTechnology = employeeTechnology;
    }
}
