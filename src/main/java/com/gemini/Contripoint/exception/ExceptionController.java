package com.gemini.Contripoint.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionController {
    public static final Logger log = LoggerFactory.getLogger(ExceptionController.class);

    @ExceptionHandler(value = ContripointException.class)
    public ResponseEntity<String> contripointException(ContripointException e) {
        log.error("Exception : ", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }

    @ExceptionHandler
    public ResponseEntity<String> defaultException(Exception e) {
        log.error("Exception : ", e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
