package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.config.StartUpInit;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.model.UserInfo;
import com.gemini.Contripoint.repository.implementation.EmployeeRepositoryImp;
import com.gemini.Contripoint.repository.implementation.NotificationRepositoryImp;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import com.gemini.Contripoint.service.interfaces.TokenService;
import com.gemini.Contripoint.utils.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;


@Service
public class TokenServiceImp implements TokenService {

    public static final Logger log = LoggerFactory.getLogger(TokenServiceImp.class);

    @Autowired
    JwtTokenUtils jwtTokenUtils;
    @Autowired
    ContributionsRepository contributionsRepository;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private StartUpInit startUpInit;
    @Autowired
    private NotificationRepositoryImp notificationRepositoryImp;

    public ResponseEntity authenticate(@RequestBody String email) {
        log.debug("Inside authenticate() with parameters {}", email);
        email = email.toLowerCase();
        Employee details = employeeRepositoryImp.getEmployeeDetails(email); // Getting employee details from employee table
        boolean isManager = false;      // Setting default value
        boolean notifications = false;
        boolean isAdmin = false;
        boolean isAdminManager = false;
        if (details == null) { // if employee is not present
            startUpInit.init(); // Refreshing the table form MIS API
            details = employeeRepositoryImp.getEmployeeDetails(email); // Getting details
            if (details == null) { // Still if employee is not present
                throw new ContripointException("No user found. Please try again later", HttpStatus.NOT_FOUND);
            }
        }
        isManager = employeeRepositoryImp.checkIsManger(details.getId()); // Checking if employee is manager
        isAdmin = employeeRepositoryImp.checkIsAdmin(email);
        isAdminManager = employeeRepositoryImp.checkIsAdminManager(details.getId());
        notifications = notificationRepositoryImp.checkUnreadNotifications(details.getId());    // checking if employee has any notifications
        int points = notificationRepositoryImp.calculatePoints(details.getId()).orElse(0); // checking for points
        String notificationMessage;
        if (points == 0) {
            notificationMessage = "";
            notifications = false;
        } else {
            notificationMessage = "Congratulations! You are awarded " + points + " points. Thank you for your contributions! Kindly click on the bell icon to view unread notifications.";  // setting login message
        }
//        String name = (String) payload.get("name");
        String name = details.getName();
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(email);
        userInfo.setName(name);
//        userInfo.setUserId(userId);
        userInfo.setUserId(details.getId());
        String jwtToken = jwtTokenUtils.generateToken(userInfo);
        userInfo.setToken(jwtToken);
        String t = userInfo.getToken();
        ResponseMessage message = new ResponseMessage(details, isManager, isAdmin, isAdminManager, notifications, notificationMessage, t);
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
