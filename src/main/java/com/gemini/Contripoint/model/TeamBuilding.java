package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "team_building")
public class TeamBuilding {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int headcount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHeadcount() {
        return headcount;
    }

    public void setHeadcount(int headcount) {
        this.headcount = headcount;
    }
}