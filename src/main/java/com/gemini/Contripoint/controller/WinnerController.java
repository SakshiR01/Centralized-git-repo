package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.WinnerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class WinnerController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    private WinnerService winnerService;

    @PostMapping("/publish")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addRewards(@RequestBody String winnerDetails) throws IOException {
        log.info("Inside addRewards() (Controller) with parameters {} ", winnerDetails);
        ResponseMessage responseMessage = new ResponseMessage(null, winnerService.addRewards(winnerDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

}
