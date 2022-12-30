package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.SelfDevelopmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class SelfDevelopmentController {

    public static final Logger log = LoggerFactory.getLogger(SelfDevelopmentController.class);

    @Autowired
    private SelfDevelopmentService selfDevelopmentService;

    @PostMapping(value = "/addselfdevelopment")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addSelfDevelopment(@RequestBody String selfDevelopmentDetails) throws IOException {
        log.info("Inside addSelfDevelopment() with parameters {} ", selfDevelopmentDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, selfDevelopmentService.addSelfDevelopment(selfDevelopmentDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/saveselfdevelopmentasdraft")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveAsDraftSelfDevelopment(@RequestBody String selfDevelopmentDetails) throws IOException {
        log.info("Inside saveAsDraftSelfDevelopment() with parameters {} ", selfDevelopmentDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, selfDevelopmentService.saveAsDraftSelfDevelopment(selfDevelopmentDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/fetchsingleselfdevelopment", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> fetchSingleSelfDevelopment(@RequestBody int id) throws IOException {
        log.info("Inside fetchSingleSelfDevelopment() with parameters {} ", id);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, selfDevelopmentService.fetchSingleSelfDevelopment(id));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/fetchallselfdevelopment")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllSelfDevelopment(@RequestBody String data) {
        log.info("Inside getAllSelfDevelopment() with parameters {} ", data);

        return new ResponseEntity(selfDevelopmentService.getAllSelfDevelopment(data), HttpStatus.OK);
    }

    @PostMapping(value = "/deleteselfdevelopment")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteSelfDevelopment(@RequestBody int id) throws IOException {
        log.info("Inside deleteSelfDevelopment() with parameters {} ", id);
        ResponseMessage<String> responseMessage = new ResponseMessage<String>(null, selfDevelopmentService.deleteSelfDevelopment(id));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
