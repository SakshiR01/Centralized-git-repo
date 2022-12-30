package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Notification;
import com.gemini.Contripoint.model.NotificationMessage;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.NotificationRepository;
import com.gemini.Contripoint.service.interfaces.NotificationService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
public class NotificationController {

    Logger log = LoggerFactory.getLogger(NotificationController.class);
    @Autowired
    NotificationRepository notificationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/notify")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public void getLiveNotification(@RequestBody List<String> empIds, ArrayList<Integer> notificationId, boolean bulkApproval) throws IOException {
        log.info("Inside getNotification with parameters {} {} {}", empIds, notificationId, bulkApproval);
        List<String> finalList = empIds.stream().distinct().collect(Collectors.toList());   // Creating a new List of distinct employee
        List<List<String>> ListOfSegregatedNotificationIds = new ArrayList<>(); // Creating a list of list containing notification Id's wrt employee notification.
        for (int i = 0; i < finalList.size(); i++) {
            List<String> one = new ArrayList<>();
            for (int j = 0; j < notificationId.size(); j++) {
                Notification notification = notificationRepository.getById(notificationId.get(j));
                if (finalList.get(i).equalsIgnoreCase(notification.getEmpId())) {
                    one.add(notificationId.get(j).toString());
                }
            }
            ListOfSegregatedNotificationIds.add(one);
        }
        for (int i = 0; i < ListOfSegregatedNotificationIds.size(); i++) {
            NotificationMessage notificationMessage = notificationService.getLiveNotification(finalList.get(i), ListOfSegregatedNotificationIds.get(i), bulkApproval);
            template.convertAndSendToUser(finalList.get(i), "/topic/notification", notificationMessage); // Sending Live notification to specific user.
        }
    }

    @PostMapping("/notificationcount")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public Integer notificationCount(@RequestBody String empId) throws IOException {
        log.info("Inside notificationCount with parameters {}", empId);
        return notificationService.notificationCount(empId);
    }

    @PostMapping("/getnotifications")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity getNotifications(@RequestBody String data) throws IOException, JSONException {
        log.info("Inside previousNotification with parameters {}", data);
        // Parsing String employeeId and pageNo from String JSON Data.
        JSONObject jsonObject = new JSONObject(data);
        String empId = jsonObject.get("empId").toString();
        int pageNo = jsonObject.getInt("pageNo");
        pageNo--;
        ResponseMessage message = new ResponseMessage(null, notificationService.getNotifications(empId, pageNo));
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/markasread")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity markAsRead(@RequestBody String empId) {
        log.info("Inside markAsRead with parameters {}", empId);
        ResponseMessage message = new ResponseMessage(null, notificationService.markAsRead(empId));
        return new ResponseEntity(message, HttpStatus.OK);
    }
}
