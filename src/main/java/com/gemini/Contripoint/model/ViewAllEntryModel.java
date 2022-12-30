package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ViewAllEntryModel {

    private String message;
    private String employeeId;
    private String employeeName;
    private String empDesignation;
    private Entry entry;
    private byte[] image;
    private Long totalRows;

    public ViewAllEntryModel(String employeeId, String employeeName, String empDesignation, Entry entry, byte[] image, Long totalRows) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.empDesignation = empDesignation;
        this.entry = entry;
        this.image = image;
        this.totalRows = totalRows;
    }

    public ViewAllEntryModel(String message, Long totalRows) {
        this.message = message;
        this.totalRows = totalRows;
    }

    public ViewAllEntryModel(String employeeId, String employeeName, String empDesignation, byte[] image) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.empDesignation = empDesignation;
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmpDesignation() {
        return empDesignation;
    }

    public void setEmpDesignation(String empDesignation) {
        this.empDesignation = empDesignation;
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Long getTotalRows() {
        return totalRows;
    }

    public void setTotalRows(Long totalRows) {
        this.totalRows = totalRows;
    }
}
