package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@Entity
@Table(name = "technologies")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Technologies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String technology;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
}
