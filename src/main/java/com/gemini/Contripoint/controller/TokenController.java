package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.service.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class TokenController {

    @Autowired
    TokenService tokenService;

    @PostMapping(value = "/api/authenticate")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity getDetails(@RequestBody String email) throws GeneralSecurityException, IOException, IllegalAccessException {
        return new ResponseEntity(tokenService.authenticate(email), HttpStatus.ACCEPTED);
    }
}