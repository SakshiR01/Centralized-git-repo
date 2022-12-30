package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.repository.interfaces.AdminRepository;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class UpdateRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(UpdateRepositoryImp.class);
    @Autowired
    EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    ContributionsRepository contributionsRepository;

    @Autowired
    AdminRepository adminRepository;

    public void updateManager(String empId, String rmId) throws IOException {
        log.debug("Inside updateManager (Repository) with parameters {} {}", empId, rmId);
        employeeRepositoryImp.updateManager(empId, rmId);
    }

    public String archive() {
        contributionsRepository.archive();
        return "Archived all Contributions!";
    }

    public String createAdmin() {
        List<String> admins = adminRepository.getAllAdmin();
        if (admins.size() != 0) {
            admins.forEach(admin -> {
                employeeRepositoryImp.setAdmin(admin);
            });
        }
        return "Success";
    }
}