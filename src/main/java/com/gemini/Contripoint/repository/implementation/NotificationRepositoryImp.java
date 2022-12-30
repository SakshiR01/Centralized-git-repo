package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.model.NotificationMessage;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.repository.interfaces.NotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImp {

    Logger log = LoggerFactory.getLogger(NotificationRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;

    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private NotificationRepository notificationRepository;

    public NotificationMessage getLiveNotification(String empId, List<Integer> notificationId, boolean bulkApproval) {

        log.debug("Inside getNotification (Repository) with parameters {} {} {}", empId, notificationId, bulkApproval);
        if (!bulkApproval) {    // checking for bulkApproval
            NotificationMessage message = new NotificationMessage(contributionsRepository.getLiveNotification(empId, notificationId.get(0))); // Creating a new Notification Message
            return message;
        } else {
            String type = "approved";
            if (notificationRepository.getById(notificationId.get(0)).getNotificationMessage().contains("declined")) {   // Setting type as declined
                type = "declined";
            }
            ArrayList<ArrayList<String>> notification = new ArrayList<>();
            for (int i = 0; i < notificationId.size(); i++) {
                notification.add(contributionsRepository.getLiveNotification(empId, notificationId.get(i)));
            }
            int size = notification.size();  // setting notifications size
            // Setting notification message
            String message = employeeRepositoryImp.getEmployeeName(empId) + "! Your " + size + " contributions has been " + type + " by " + employeeRepositoryImp.getEmployeeName(employeeRepositoryImp.getProfileData(empId).getRmId());
            ArrayList<String> returnedMessage = new ArrayList<>();
            returnedMessage.add(message);
            return new NotificationMessage(returnedMessage);
        }
    }

    public Integer notificationCount(String empId) {

        log.debug("Inside notificationCount (Repository) with parameters {}", empId);
        return notificationRepository.notificationCount(empId);  // Getting Notificatio count
    }

    public ArrayList<NotificationMessage> getNotifications(String empId, int pageNo) {

        log.debug("Inside previousNotification (Repository) with parameters {}", empId);
        Pageable pageable = PageRequest.of(pageNo, 5);  // Getting page size as 5
        Page<String> notifications = notificationRepository.getNotificationMessage(empId, pageable); // getting notification message
        Page<Integer> read = notificationRepository.getNotificationread(empId, pageable);   // getting notification status
        Page<String> type = notificationRepository.getNotificationType(empId, pageable);    // getting notification type
        Page<String> modified = notificationRepository.getLastModifiedOn(empId, pageable);  // getting notification modified time

        ArrayList<NotificationMessage> responseMessageList = new ArrayList<>();
        for (int i = 0; i < notifications.getContent().size(); i++) { // looping over notification size
            String status = notifications.getContent().get(i).contains("approved") ? "Approved" : "Rejected"; // Setting status
            String notificationType = type.getContent().get(i);     // getting notification type
            String modifiedOn = modified.getContent().get(i); // getting modified on
            String[] parsedtime = modifiedOn.split(" ");
            String date = parsedtime[0];    // getting date
            String time = parsedtime[1].substring(0, 8);    // getting time
            NotificationMessage notification = new NotificationMessage(notifications.getContent().get(i), read.getContent().get(i), status, notificationType, date, time);
            responseMessageList.add(notification);
        }
        return responseMessageList;
    }

    public String markAsRead(String empId) {

        log.debug("Inside markAsRead (Repsoitory) with parameter {}", empId);
        notificationRepository.markAsRead(empId); // Marking as Read for specific employee id
        return "Done";
    }

    public boolean checkUnreadNotifications(String empId) {
        log.debug("Inside checkUnreadNotifications (Repository) with parameters {}", empId);
        List<String> notification = (notificationRepository.checkUnreadNotifications(empId)); // checking for unread notificaion
        return !notification.isEmpty();
    }

    public Optional<Integer> calculatePoints(String id) {
        log.debug("Inside calculatePoints with (Repository) parameters {}", id);
        return notificationRepository.calculatePoints(id); // Calculating total points of unread notifications
    }
}
