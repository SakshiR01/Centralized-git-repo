package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.SelfDevelopmentRepositoryImp;
import com.gemini.Contripoint.service.interfaces.SelfDevelopmentService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SelfDevelopmentServiceImp implements SelfDevelopmentService {

    public static final Logger log = LoggerFactory.getLogger(SelfDevelopmentServiceImp.class);

    @Autowired
    private SelfDevelopmentRepositoryImp selfDevelopmentRepositoryImp;

    @Override
    public Integer addSelfDevelopment(String selfDevelopmentDetails) throws IOException {
        log.debug("Inside addSelfDevelopment() with parameters {}", selfDevelopmentDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions selfDevelopment = objectMapper.readValue(selfDevelopmentDetails, Contributions.class); // Mapping details to Contribution class.
            return selfDevelopmentRepositoryImp.addSelfDevelopment(selfDevelopment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public Integer saveAsDraftSelfDevelopment(String selfDevelopmentDetails) throws IOException {
        log.debug("Inside saveAsDraftSelfDevelopment() with parameters {}", selfDevelopmentDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions selfDevelopment = objectMapper.readValue(selfDevelopmentDetails, Contributions.class); // Mapping details to Contribution class.
            return selfDevelopmentRepositoryImp.saveAsDraftSelfDevelopment(selfDevelopment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllSelfDevelopment(String data) {
        log.debug("Inside getAllSelfDevelopment() with parameters {}", data);
        try {
            // Extracting JSON String data to employeeId and pageNo
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return selfDevelopmentRepositoryImp.getAllSelfDevelopment(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions fetchSingleSelfDevelopment(int selfDevelopmentId) throws IOException {
        log.debug("Inside fetchSingleSelfDevelopment() with parameters {}", selfDevelopmentId);
        try {

            return selfDevelopmentRepositoryImp.fetchSingleSelfDevelopment(selfDevelopmentId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteSelfDevelopment(int selfDevelopmentId) throws IOException {
        log.debug("Inside deleteSelfDevelopment() with parameters {}", selfDevelopmentId);
        try {
            return selfDevelopmentRepositoryImp.deleteSelfDevelopment(selfDevelopmentId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}