package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.TrainingSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TrainingSessionController {

    public static final Logger log = LoggerFactory.getLogger(TrainingSessionController.class);

    @Autowired
    TrainingSessionService trainingSessionService;

    @RequestMapping(value = "/addtrainingsession", method = {RequestMethod.POST})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> addTrainingSession(@RequestBody String formDetails) throws IOException {
        log.info("Inside addTrainingSession with parameters {}", formDetails);
        ResponseMessage responseMessage = new ResponseMessage<>(null, trainingSessionService.addTrainingSession(formDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/savetrainingsessionasdraft", method = {RequestMethod.POST})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> saveTraingSessionAsDraft(@RequestBody String formDetails) throws IOException {
        log.info("Inside addTrainingSession with parameters {}", formDetails);
        ResponseMessage responseMessage = new ResponseMessage<>(null, trainingSessionService.saveTrainingSessionAsDraft(formDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/gettrainingsessions")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getTrainingSessions(@RequestBody String data) throws IOException {
        log.info("Inside getTrainingSession with parameters {}", data);
        return new ResponseEntity(trainingSessionService.getTrainingSession(data), HttpStatus.OK);
    }

    @PostMapping("/getsingletrainingsession")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> getSingleTrainingSession(@RequestBody Integer id) throws IOException {
        log.info("Inside getSingleTrainingSession with parameters {}", id);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, trainingSessionService.getSingleTrainingSession(id));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/deletetrainingsession")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteTrainingSession(@RequestBody int id) throws IOException {
        log.info("Inside delete mentorship");
        ResponseMessage<String> message = new ResponseMessage<>(null, trainingSessionService.deleteTrainingSession(id));
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
