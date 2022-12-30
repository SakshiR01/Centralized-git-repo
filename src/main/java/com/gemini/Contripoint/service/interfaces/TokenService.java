package com.gemini.Contripoint.service.interfaces;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public interface TokenService {

    ResponseEntity authenticate(@RequestBody String token) throws GeneralSecurityException, IOException, IllegalAccessException;

}
