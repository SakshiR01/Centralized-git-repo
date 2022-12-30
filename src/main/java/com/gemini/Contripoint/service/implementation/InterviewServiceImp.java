package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.InterviewRepositoryImp;
import com.gemini.Contripoint.service.interfaces.InterviewService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class InterviewServiceImp implements InterviewService {

    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    private InterviewRepositoryImp interviewRepositoryImp;

    @Override
    public Integer addInterview(String interviewDetails) throws IOException {
        log.debug("Inside addInterview (Service) with parameters {}", interviewDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions interview = objectMapper.readValue(interviewDetails, Contributions.class);    // Using Object Mapper to map data to Contribution class
            return interviewRepositoryImp.addInterview(interview);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveasDraftInterview(String interviewDetails) throws IOException {
        log.debug("Inside saveasDraftInterview (Service) with parameters {}", interviewDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions interview = objectMapper.readValue(interviewDetails, Contributions.class);    // Using Object Mapper to map data to Contribution class
            return interviewRepositoryImp.saveasDraftInterview(interview);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllInterview(String data) throws IOException {
        log.debug("Inside getAllInterview (Service) with parameters {}", data);
        try {
            // Parsing employeeId and pageNo from JSON String data.
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return interviewRepositoryImp.getAllInterview(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions fetchSingleInterview(int interviewId) throws IOException {
        log.debug("Inside fetchSingleInterview (Service) with parameters {}", interviewId);
        try {
            return interviewRepositoryImp.fetchSingleEvaluation(interviewId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getProfile() throws IOException {
        log.debug("Inside getProfiles (Service) with no parameters");
        try {
            return interviewRepositoryImp.getProfile();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getExperience() {
        log.debug("Inside getExperience (Service) with no parameters");
        try {
            return interviewRepositoryImp.getExperience();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> getMonth() {
        log.debug("Inside getMonth (Service) with no parameters");
        try {
            return interviewRepositoryImp.getMonth();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteInterview(int id) throws IOException {
        log.debug("Inside deleteInterview() with parameters {}", id);
        try {
            return interviewRepositoryImp.deleteInterview(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
