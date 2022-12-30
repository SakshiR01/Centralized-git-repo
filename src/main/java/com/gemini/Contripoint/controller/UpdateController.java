package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.service.interfaces.UpdateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class UpdateController {

    public static final Logger log = LoggerFactory.getLogger(UpdateController.class);
    @Autowired
    UpdateService updateService;

    @PostMapping(value = "/updateManager")
    public void updateManager(String empId, String rmId) throws IOException {
        log.info("Inside updateManager (Controller) with parameters {} {}", empId, rmId);
        updateService.updateManager(empId, rmId);
    }

    //    @Scheduled(cron = "0 30 18 1 4 *")   // Scheduling to run the function on 1st of April Every year.
    public ResponseEntity archive() throws IOException {
        return new ResponseEntity(updateService.archive(), HttpStatus.OK);
    }

    @PostMapping(value = "/update/admin")
    @CrossOrigin("*")
    @Scheduled(cron = "0 5 0 * * *")
    public ResponseEntity createAdmin() {
        return new ResponseEntity(updateService.createAdmin(), HttpStatus.OK);
    }
}
