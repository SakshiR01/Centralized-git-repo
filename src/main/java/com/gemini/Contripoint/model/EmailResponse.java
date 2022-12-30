package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EmailResponse {

    private String message;
    private boolean status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
