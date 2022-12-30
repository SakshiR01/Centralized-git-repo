package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.InterviewService;
import com.gemini.Contripoint.service.interfaces.WrapperModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class InterviewController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    private WrapperModelService wrapperModelService;

    @Autowired
    private InterviewService interviewService;

    @PostMapping(value = "/addinterview")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addInterview(@RequestBody String interviewDetails) throws IOException {
        log.info("Inside addInterview() with parameters {} ", interviewDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, interviewService.addInterview(interviewDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/saveasdraftinterview")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveasDraftInterview(@RequestBody String interviewDetails) throws IOException {
        log.info("Inside saveasdraft() with parameters {}", interviewDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, interviewService.saveasDraftInterview(interviewDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/getallinterview")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllInterview(@RequestBody String data) throws IOException {
        log.info("Inside getAllInterview() with parameters {}", data);
        return new ResponseEntity(interviewService.getAllInterview(data), HttpStatus.OK);
    }

    @PostMapping(value = "/selectedinterview", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> fetchSingleInterview(@RequestBody int interviewId) throws IOException {
        log.info("Inside fetchSingleInterview() with parameters {}", interviewId);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, interviewService.fetchSingleInterview(interviewId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/getprofiles")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<String> getProfiles() throws IOException {
        log.info("Inside getProfiles with no parameters");
        return interviewService.getProfile();
    }

    @PostMapping(value = "/getexperience")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<String> getExperience() throws IOException {
        log.info("Inside getExperience() with no parameters");
        return interviewService.getExperience();
    }

    @PostMapping(value = "/getmonth")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public List<String> getMonth() throws IOException {
        log.info("Inside getMonth() with no parameters");
        return interviewService.getMonth();
    }

    @PostMapping(value = "/deleteinterview")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteInterview(@RequestBody int id) throws IOException {
        log.info("Inside delete interview");
        ResponseMessage<String> message = new ResponseMessage<>(null, interviewService.deleteInterview(id));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
