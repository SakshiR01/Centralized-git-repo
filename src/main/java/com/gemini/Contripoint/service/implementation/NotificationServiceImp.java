package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.NotificationMessage;
import com.gemini.Contripoint.repository.implementation.NotificationRepositoryImp;
import com.gemini.Contripoint.service.interfaces.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationServiceImp implements NotificationService {

    Logger log = LoggerFactory.getLogger(NotificationServiceImp.class);

    @Autowired
    private NotificationRepositoryImp notificationRepositoryImp;

    @Override
    public NotificationMessage getLiveNotification(String empId, List<String> notificationId, boolean bulkApproval) throws IOException {
        log.debug("Inside getNotification (Service) with parameters {} {} {}", empId, notificationId, bulkApproval);
        try {
            List<Integer> notifications = notificationId.stream().map(id -> {
                int notification = Integer.parseInt(id);
                return notification;
            }).collect(Collectors.toList());
            return notificationRepositoryImp.getLiveNotification(empId, notifications, bulkApproval);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public Integer notificationCount(String empId) {
        log.debug("Inside notificationCount (Service) with parameters {}", empId);
        try {
            return notificationRepositoryImp.notificationCount(empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ArrayList<NotificationMessage> getNotifications(String empId, int pageNo) {
        log.debug("Inside getNotifications (Service) with parameters {}, {}", empId, pageNo);
        try {
            return notificationRepositoryImp.getNotifications(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String markAsRead(String empId) {
        log.debug("Inside markAsRead (Service) with parameter {}", empId);
        try {
            return notificationRepositoryImp.markAsRead(empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
