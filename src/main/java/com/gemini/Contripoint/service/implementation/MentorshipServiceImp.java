package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.MentorshipRepositoryImp;
import com.gemini.Contripoint.service.interfaces.MentorshipService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MentorshipServiceImp implements MentorshipService {

    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    private MentorshipRepositoryImp mentorshipRepositoryImp;

    @Override
    public Integer addMentorship(String mentorshipDetails) throws IOException {
        log.debug("Inside addMentorship() with parameters {}", mentorshipDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions mentorship = objectMapper.readValue(mentorshipDetails, Contributions.class); // Mapping mentorshipDetails with Contribution Class
            return mentorshipRepositoryImp.addMentorship(mentorship);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveasDraftMentorship(String mentorshipDetails) throws IOException {
        log.debug("Inside saveasDraftMentorship() with parameters {}", mentorshipDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions mentorship = objectMapper.readValue(mentorshipDetails, Contributions.class); // Mapping mentorshipDetails with Contribution Class
            return mentorshipRepositoryImp.saveasDraftMentorship(mentorship);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllMentorship(String data) {
        log.debug("Inside getAllMentorship() with parameters {}", data);
        try {
            // Parsing employeeId and pageNo from JSON String data
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return mentorshipRepositoryImp.getAllMentorship(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions fetchSingleMentorship(int mentorshipId) throws IOException {
        log.debug("Inside fetchSingleMentorship() with parameters {}", mentorshipId);
        try {
            return mentorshipRepositoryImp.fetchSingleMentorship(mentorshipId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage fetchEmployeeDropDown() throws IOException {
        log.debug("Inside fetchEmployeeDropDown() with no parameters ");
        try {
            return mentorshipRepositoryImp.fetchEmployeeDropDown();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<String> fetchTechnologies() throws IOException {
        log.debug("Inside fetchTechnologies() with no parameters ");
        try {
            return mentorshipRepositoryImp.fetchTechnologies();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteMentorship(int id) throws IOException {
        log.debug("Inside deleteMentorship() with parameters {}", id);
        try {
            return mentorshipRepositoryImp.deleteMentorship(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
