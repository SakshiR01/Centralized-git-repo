package com.gemini.Contripoint.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Table(name = "experience")
public class Experience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String level;

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
