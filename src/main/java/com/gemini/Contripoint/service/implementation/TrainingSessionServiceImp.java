package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.TrainingSessionRepositoryImp;
import com.gemini.Contripoint.service.interfaces.TrainingSessionService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TrainingSessionServiceImp implements TrainingSessionService {

    public static final Logger log = LoggerFactory.getLogger(TrainingSessionServiceImp.class);

    @Autowired
    private TrainingSessionRepositoryImp trainingSessionRepositoryImp;

    @Override
    public Integer addTrainingSession(String formDetails) throws IOException {
        log.debug("Inside TrainingSession (Service) with parameters ", formDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions contributions = objectMapper.readValue(formDetails, Contributions.class); // Mapping details to Contribution class
            return trainingSessionRepositoryImp.addTrainingSession(contributions);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveTrainingSessionAsDraft(String formDetails) throws IOException {
        log.debug("Inside saveTrainingSessionAsDraft (Service) with parameters {}", formDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions contributions = objectMapper.readValue(formDetails, Contributions.class); // Mapping details to Contribution class
            return trainingSessionRepositoryImp.saveTrainingSessionAsDraft(contributions);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getTrainingSession(String data) throws IOException {
        log.debug("Inside getTrainingSession (Service) with parameters {} {}", data);
        try {
            // Extracting employeeId and pageNo from JSON String data.
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.getString("empId");
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return trainingSessionRepositoryImp.getTrainingSession(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions getSingleTrainingSession(Integer id) throws IOException {
        log.debug("Inside getSingleTrainingSession (Service) with parameters {}", id);
        try {
            return trainingSessionRepositoryImp.getSingleTrainingSession(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteTrainingSession(int id) throws IOException {
        log.debug("Inside deleteMentorship() with parameters {}", id);
        try {
            return trainingSessionRepositoryImp.deleteTrainingSession(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
