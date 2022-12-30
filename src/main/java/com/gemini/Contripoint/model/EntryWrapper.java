package com.gemini.Contripoint.model;

import com.sun.istack.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class EntryWrapper {

    private String entryDetails;
    private Integer eventId;
    private String empId;
    @Nullable
    private MultipartFile entryFile;

    public String getEntryDetails() {
        return entryDetails;
    }

    public void setEntryDetails(String entryDetails) {
        this.entryDetails = entryDetails;
    }

    @Nullable
    public MultipartFile getEntryFile() {
        return entryFile;
    }

    public void setEntryFile(MultipartFile entryFile) {
        this.entryFile = entryFile;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }
}
