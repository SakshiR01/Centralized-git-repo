package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.model.WrapperModel;
import com.gemini.Contripoint.service.interfaces.ClientFeedbackService;
import com.gemini.Contripoint.service.interfaces.WrapperModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ClientFeedbackController {

    public static final Logger log = LoggerFactory.getLogger(ClientFeedbackController.class);

    @Autowired
    private WrapperModelService wrapperModelService;

    @Autowired
    private ClientFeedbackService clientFeedbackService;

    @PostMapping(value = "/addclientfeedback", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addClientFeedback(@ModelAttribute WrapperModel feedbackDetails) throws IOException {
        log.info("Inside addClientFeedback with parameters {}", feedbackDetails.getFormDetails());
        ResponseMessage<Integer> responseMessage = new ResponseMessage<>(null, wrapperModelService.addClientFeedback(feedbackDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/previousevaluations")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> previousEvaluation(@RequestBody String data) throws IOException {
        log.info("Inside previousEvaluation with parameters {} ", data);
        return new ResponseEntity(clientFeedbackService.previousEvaluation(data), HttpStatus.OK);
    }

    @PostMapping(value = "/selectedevaluation", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> fetchSingleEvaluation(@RequestBody int feedbackId) throws IOException {
        log.info("Inside fetchSingleEvaluation with parameters {}", feedbackId);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage<>(null, clientFeedbackService.fetchSingleEvaluation(feedbackId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/saveasdraft", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveAsDraft(@ModelAttribute WrapperModel feedbackDetails) throws IOException {
        log.info("Inside addClientFeedback with parameters {}", feedbackDetails.getFormDetails());
        ResponseMessage<Integer> responseMessage = new ResponseMessage<>(null, wrapperModelService.saveAsADraft(feedbackDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/downloadattachment")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<byte[]>> downloadAttachment(@RequestBody int id) throws IOException {
        log.info("Inside downloadAttachment with parameters {}", id);
        ResponseMessage<byte[]> responseMessage = new ResponseMessage<>(null, clientFeedbackService.downloadAttachment(id));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/deleteclientfeedback")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteClientFeedback(@RequestBody int contributionId) throws IOException {
        log.debug("Inside deleteClientFeedback() with parameters {}", contributionId);
        ResponseMessage<String> responseMessage = new ResponseMessage<String>(null, clientFeedbackService.deleteClientFeedback(contributionId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
