package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.MentorshipService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

//LOOK AT IT
@RestController
public class MentorshipController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);


    @Autowired
    private MentorshipService mentorshipService;

    @PostMapping(value = "/addmentorship")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addMentorship(@RequestBody String mentorshipDetails) throws IOException {
        log.info("Inside addMentorship() with parameters {} ", mentorshipDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, mentorshipService.addMentorship(mentorshipDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/saveasdraftmentorship")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveasDraftMentorship(@RequestBody String mentorshipDetails) throws IOException {
        log.info("Inside saveasdraftmentorship() with parameters {}", mentorshipDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, mentorshipService.saveasDraftMentorship(mentorshipDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/getallmentorship")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllMentorship(@RequestBody String data) {
        log.info("Inside getAllMentorship() with parameters {}", data);
        return new ResponseEntity(mentorshipService.getAllMentorship(data), HttpStatus.OK);
    }

    @PostMapping(value = "/selectedmentorship", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> fetchSingleMentorship(@RequestBody int mentorshipId) throws IOException {
        log.info("Inside fetchSingleMentorship() with parameters {}", mentorshipId);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, mentorshipService.fetchSingleMentorship(mentorshipId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/fetchemployeedropdown")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> fetchEmployeeDropDown() throws IOException {
        log.info("Inside fetchEmployeeDropDown() ");
        return new ResponseEntity<>(mentorshipService.fetchEmployeeDropDown(), HttpStatus.OK);
    }

    @PostMapping(value = "/gettechnologies")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<String> fetchTechnologies() throws IOException {
        log.info("Inside fetchTechnologies");
        return mentorshipService.fetchTechnologies();
    }

    @PostMapping(value = "/deletementorship")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteMentorship(@RequestBody int id) throws IOException {
        log.info("Inside delete mentorship");
        ResponseMessage<String> message = new ResponseMessage(null, mentorshipService.deleteMentorship(id));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
