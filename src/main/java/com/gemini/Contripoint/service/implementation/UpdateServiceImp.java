package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.repository.implementation.UpdateRepositoryImp;
import com.gemini.Contripoint.service.interfaces.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class UpdateServiceImp implements UpdateService {

    public static final Logger log = LoggerFactory.getLogger(UpdateServiceImp.class);

    @Autowired
    UpdateRepositoryImp updateRepositoryImp;

    @Override
    public void updateManager(String empId, String rmId) throws IOException {
        log.debug("Inside updateManager (Service) with parameters {} {}", empId, rmId);
        try {
            updateRepositoryImp.updateManager(empId, rmId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String archive() {
        try {
            return updateRepositoryImp.archive();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String createAdmin() {
        try {
            return updateRepositoryImp.createAdmin();
        } catch (Exception e) {
            throw new ContripointException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

