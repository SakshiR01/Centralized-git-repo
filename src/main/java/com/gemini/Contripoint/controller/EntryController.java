package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.EntryResponseMessage;
import com.gemini.Contripoint.model.EntryWrapper;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.EntryService;
import com.gemini.Contripoint.service.interfaces.EntryWrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class EntryController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    public EntryWrapperService entryWrapperService;

    @Autowired

    EntryService entryService;

    @PostMapping("/add/participant/entry")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addEntry(@ModelAttribute EntryWrapper entryWrapper) throws IOException {
        log.info("Inside EntryController with parameters {}", entryWrapper);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, entryWrapperService.addEntry(entryWrapper));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    @PostMapping("/view/all/entry")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<ResponseMessage>> viewAllEntries(@RequestBody String data) throws IOException {
        log.info("Inside EntryController(view all entry) with parameters {}", data);
        return new ResponseEntity(entryService.viewAllEntries(data), HttpStatus.OK);
    }

    @PostMapping("/find/submitted/pending/entry")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<EntryResponseMessage> findListOfEntries(@RequestBody String id) throws IOException {
        log.info("Inside findList of Entries with parameters {}", id);
        EntryResponseMessage entryResponseMessage = entryService.findListOfEntries(id);
        return new ResponseEntity<EntryResponseMessage>(entryResponseMessage, HttpStatus.OK);
    }

    @PostMapping("/get/single/entry")
    @CrossOrigin(origins = "*")
    public ResponseEntity getSingleEntry(@RequestBody String entryId) {
        log.info("Inside getSingleEntry (Controller) with parameters {}", entryId);
        return new ResponseEntity(entryService.getSingleEntry(entryId), HttpStatus.OK);
    }

    @PostMapping("/publish/certificates/admin")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> publishCertificate(@RequestBody String eventId) throws IOException {
        log.info("Inside publish Certficates() (Controller) with parameters {} ", eventId);
        ResponseMessage responseMessage = new ResponseMessage(null, entryService.publishCertificates(eventId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/get/participants")
    @CrossOrigin("*")
    @ResponseBody
    public ResponseEntity getEnrolledNotEnrolled(@RequestBody String data) {
        log.info("Inside getEnrolledNotEntrolled with parameters", data);
        return new ResponseEntity<>(entryService.getEnrolledNotEnrolled(data), HttpStatus.OK);
    }
}
