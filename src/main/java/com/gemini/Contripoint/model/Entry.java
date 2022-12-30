package com.gemini.Contripoint.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String summary;
    private Date createdOn;
    private Date lastModifiedOn;
    private StringBuilder fileURL;
    private String fileName;
    private String description;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getLastModifiedOn() {
        return lastModifiedOn;
    }

    public void setLastModifiedOn(Date lastModifiedOn) {
        this.lastModifiedOn = lastModifiedOn;
    }

    public StringBuilder getFileURL() {
        return fileURL;
    }

    public void setFileURL(StringBuilder fileURL) {
        this.fileURL = fileURL;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
