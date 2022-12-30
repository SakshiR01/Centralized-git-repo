package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.ProjectService;
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
public class ProjectController {

    public static final Logger log = LoggerFactory.getLogger(InterviewController.class);

    @Autowired
    private WrapperModelService wrapperModelService;

    @Autowired
    private ProjectService projectService;

    @PostMapping(value = "/addproject")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> addProject(@RequestBody String projectDetails) throws IOException {
        log.info("Inside addProject with parameters {} ", projectDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, projectService.addProject(projectDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/saveasdraftproject")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Integer>> saveasDraftProject(@RequestBody String projectDetails) throws IOException {
        log.info("Inside saveasdraftproject() with parameters {}", projectDetails);
        ResponseMessage<Integer> responseMessage = new ResponseMessage(null, projectService.saveasDraftProject(projectDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/getallproject")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> getAllProject(@RequestBody String data) throws IOException {
        log.info("Inside getAllProject() with parameters {}", data);
        return new ResponseEntity(projectService.getAllProject(data), HttpStatus.OK);
    }

    @PostMapping(value = "/selectedproject", produces = MediaType.APPLICATION_JSON_VALUE)
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> fetchSingleProject(@RequestBody int projectId) throws IOException {
        log.info("Inside fetchSingleProject() with parameters {}", projectId);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, projectService.fetchSingleProject(projectId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping(value = "/deleteproject")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteProject(@RequestBody int projectId) throws IOException {
        log.info("Inside deleteProject() controller with parameters {}", projectId);
        ResponseMessage<String> responseMessage = new ResponseMessage(null, projectService.deleteProject(projectId));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }
}
