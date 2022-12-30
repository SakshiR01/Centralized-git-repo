package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.ProjectRepositoryImp;
import com.gemini.Contripoint.service.interfaces.ProjectService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ProjectServiceImp implements ProjectService {

    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    private ProjectRepositoryImp projectRepositoryImp;

    @Override
    public Integer addProject(String projectDetails) throws IOException {
        log.debug("Inside addProject (Service) with parameters {}", projectDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions project = objectMapper.readValue(projectDetails, Contributions.class);    // Using object mapper to map projectDetails with Contribution class
            return projectRepositoryImp.addProject(project);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveasDraftProject(String projectDetails) throws IOException {
        log.debug("Inside saveasDraftProject() with parameters {}", projectDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions project = objectMapper.readValue(projectDetails, Contributions.class); // Using object mapper to map projectDetails with Contribution class
            return projectRepositoryImp.saveasDraftProject(project);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllProject(String data) {
        log.debug("Inside getAllProject() with parameters {}", data);
        try {
            // Extracting empId and pageNo from JSON String.
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return projectRepositoryImp.getAllProject(empId, pageNo);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions fetchSingleProject(int projectId) throws IOException {
        log.debug("Inside fetchSingleProject() with parameters {}", projectId);
        try {
            return projectRepositoryImp.fetchSingleProject(projectId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteProject(int projectId) throws IOException {
        log.debug("Inside deleteProject() service with parameters {}", projectId);
        try {
            return projectRepositoryImp.deleteProject(projectId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
