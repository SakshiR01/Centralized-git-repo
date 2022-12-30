package com.gemini.Contripoint.model;

import javax.persistence.*;

@Entity
@Table
public class Month {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String month;

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
}
