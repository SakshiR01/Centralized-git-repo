package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.EventDashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;

@Controller
public class EventDashboardController {

    @Autowired
    EventDashboardService eventDashboardService;

    @Autowired
    EmailService emailService;

    @PostMapping("/view/upcoming/events")
    @CrossOrigin(origins = "*")
    public ResponseEntity viewUpcomingEvent(@RequestBody String data) {
        ResponseMessage message = new ResponseMessage(null, eventDashboardService.viewUpcomingEvent(data));
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/view/closed/events")
    @CrossOrigin(origins = "*")
    public ResponseEntity viewClosedEvent(@RequestBody String data) {
        ResponseMessage message = new ResponseMessage(null, eventDashboardService.viewClosedEvent(data));
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/my/activity")
    @CrossOrigin(origins = "*")
    public ResponseEntity myEventActivity(@RequestBody String data) {
        ResponseMessage message = new ResponseMessage(null, eventDashboardService.myEventActivity(data));
        return new ResponseEntity(message, HttpStatus.OK);
    }

    @PostMapping("/events/review")
    @CrossOrigin(origins = "*")
    public ResponseEntity reviewEvent(@RequestBody String data) {
        ResponseMessage message = new ResponseMessage(null, eventDashboardService.reviewEvent(data));
        return new ResponseEntity(message, HttpStatus.OK);
    }


    @PostMapping("/change/event/status")
    @CrossOrigin(origins = "*")
    public ResponseEntity changeStatus(@RequestBody String data) throws IOException {
        eventDashboardService.changeStatus(data);
        return new ResponseEntity(HttpStatus.OK);
    }


    @PostMapping("/automate/start/end/event")
    @Scheduled(cron = "0 0 0 * * *")     // Scheduling to run the function every day at midnight
    @CrossOrigin(origins = "*")
    public ResponseEntity startEvent() {
        eventDashboardService.startEndEvent();
        return new ResponseEntity(HttpStatus.OK);
    }
}
