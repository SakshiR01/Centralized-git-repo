package com.gemini.Contripoint.controller;


import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.RewardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class RewardController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    private RewardService rewardService;

    @PostMapping("/rewards")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> viewSingleReward(@RequestBody String data) throws IOException {
        log.info("Inside viewSingleReward() (Controller) with parameters {} ", data);
        ResponseMessage responseMessage = new ResponseMessage(null, rewardService.viewSingleReward(data));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/rewards/get/all")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllReward(@RequestBody String data) throws IOException {
        log.info("Inside getAllReward() (Controller) with parameters {} ", data);
        ResponseMessage<Event> responseMessage = new ResponseMessage(null, rewardService.getAllReward(data));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

}
