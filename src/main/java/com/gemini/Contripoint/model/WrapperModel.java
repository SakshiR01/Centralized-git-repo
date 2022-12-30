package com.gemini.Contripoint.model;

import com.sun.istack.Nullable;
import org.springframework.web.multipart.MultipartFile;

public class WrapperModel {
    private String formDetails;

    @Nullable
    private MultipartFile file;

    public String getFormDetails() {
        return formDetails;
    }

    public void setFormDetails(String formDetails) {
        this.formDetails = formDetails;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
