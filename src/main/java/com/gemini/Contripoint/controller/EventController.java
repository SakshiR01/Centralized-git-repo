package com.gemini.Contripoint.controller;


import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.EventWrapper;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.EventService;
import com.gemini.Contripoint.service.interfaces.EventWrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class EventController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    private EventService eventService;

    @Autowired
    private EventWrapperService eventWrapperService;

    @PostMapping("/create/event")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addEvent(@ModelAttribute EventWrapper eventDetails) throws IOException {
        log.info("Inside addEvent() (Controller) with parameters {} ", eventDetails);
        ResponseMessage responseMessage = new ResponseMessage(null, eventWrapperService.addEvent(eventDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/create/event/draft")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addEventAsDraft(@ModelAttribute EventWrapper eventDetails) throws IOException {
        log.info("Inside addEventAsDraft() (Controller) with parameters {} ", eventDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, eventWrapperService.addEventAsDraft(eventDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);

    }

    @PostMapping("/view/single/event")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Event>> viewSingleEvent(@RequestBody String data) throws IOException {
        log.info("Inside viewSingleEvent() (Controller) with parameters {} ", data);
        ResponseMessage<Event> responseMessage = new ResponseMessage(null, eventService.viewSingleEvent(data));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/leaderboard/event")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity leaderboard(@RequestBody Integer id) throws IOException {
        log.info("Inside leaderboard with parameters{}", id);
        return new ResponseEntity(eventService.viewEventLeaderboard(id), HttpStatus.OK);
    }

    @PostMapping("/managers")
    @CrossOrigin(origins = "*")
    public List<Employee> getManagers() throws IOException {
        log.info("Inside getManagers with no parameters");
        return eventService.getManagers();
    }

    @PostMapping("/manager/employee")
    @CrossOrigin(origins = "*")
    public List<Employee> getManagerEmployee(@RequestBody String rmId) throws IOException {
        log.debug("Inside getManagerEmployee with no parameters");
        return eventService.getManagerEmployee(rmId);
    }

    @PostMapping("/delete/event")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteEvent(@RequestBody Integer id) throws IOException {
        log.info("Inside deleteEvent() (Controller) with parameters {} ", id);
        ResponseMessage<String> responseMessage = new ResponseMessage(null, eventService.deleteEvent(id));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/enroll")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity enroll(@RequestBody String data) throws IOException {
        log.info("Inside enroll with parameters {}", data);
        return new ResponseEntity(eventService.enroll(data), HttpStatus.OK);
    }

    @PostMapping("/enroll/confirmation")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity enrollConformation(@RequestBody String data) throws IOException {
        log.info("Inside enroll with parameters {}", data);
        return new ResponseEntity(eventService.enrollConformation(data), HttpStatus.OK);
    }

    @PostMapping("/winner/list")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity winnerList(@RequestBody Integer id) throws IOException {
        log.info("Inside enroll with parameters{}", id);
        ResponseMessage rs = eventService.winnerList(id);
        return new ResponseEntity(rs, HttpStatus.OK);
    }
}
