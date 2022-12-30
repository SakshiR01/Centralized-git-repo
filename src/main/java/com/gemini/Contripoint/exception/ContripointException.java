package com.gemini.Contripoint.exception;

import org.springframework.http.HttpStatus;

public class ContripointException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;

    public ContripointException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {

        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}
