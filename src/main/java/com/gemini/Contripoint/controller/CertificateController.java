package com.gemini.Contripoint.controller;


import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.model.WrapperModel;
import com.gemini.Contripoint.service.interfaces.CertificateService;
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
public class CertificateController {

    public static final Logger log = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private WrapperModelService wrapperModelService;

    @RequestMapping(value = "/addcertificate", method = {RequestMethod.POST}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<List<Integer>>> addCertificate(@ModelAttribute WrapperModel certificateDetails) throws IOException {
        log.info("Inside addCertificate with parameters {}", certificateDetails.getFormDetails());
        ResponseMessage<List<Integer>> responseMessage = new ResponseMessage(null, wrapperModelService.addCertificate(certificateDetails));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/saveasDraft", method = {RequestMethod.POST}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_MIXED_VALUE})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveAsDraft(@ModelAttribute WrapperModel certificateDetails) throws IOException {
        log.info("Inside saveasdraft with parameters {}", certificateDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, wrapperModelService.saveasDraft(certificateDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/getcertificate")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllCertificate(@RequestBody String data) throws IOException {
        log.info("Inside getAllCertificate with parameters {}", data);
        return new ResponseEntity(certificateService.getAllCertificates(data), HttpStatus.OK);
    }

    @PostMapping("/getsinglecertificate")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> getSingleCertificate(@RequestBody Integer id) throws IOException {
        log.info("Inside getSingleCertificate with parameters {}", id);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, certificateService.getSingleCertificate(id));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/downloadcertificateattachment")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<byte[]>> downloadAttachment(@RequestBody int sno) throws IOException {
        log.info("Inside downloadAttachment with parameters {}", sno);
        ResponseMessage<byte[]> responseMessage = new ResponseMessage(null, certificateService.downloadAttachment(sno));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/deletecertificate")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteCertificate(@RequestBody int contributionId) throws IOException {
        log.info("Inside deleteCertificate() with parameters {}", contributionId);
        ResponseMessage<String> responseMessage = new ResponseMessage<String>(null, certificateService.deleteCertificate(contributionId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
