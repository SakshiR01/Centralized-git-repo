package com.gemini.Contripoint.service.interfaces;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface UpdateService {

    void updateManager(String empId, String rmId) throws IOException;

    String archive();

    String createAdmin();
}
