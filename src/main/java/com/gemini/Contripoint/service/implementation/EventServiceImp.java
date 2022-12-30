package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.EventRepositoryImpl;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.EventService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EventServiceImp implements EventService {

    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    private EventRepositoryImpl eventRepositoryImpl;

    @Autowired
    private EmailService emailService;

    @Override
    public Integer addEvent(Event eventDetails) throws IOException {
        log.debug("Inside addEvent() (Service) with parameters {}", eventDetails);
        try {
            return eventRepositoryImpl.addEvent(eventDetails);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage viewSingleEvent(String data) {
        log.debug("Inside viewSingleEvent (Service) with parameters {} ", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int id = jsonObject.getInt("id");
            String empId = jsonObject.getString("empId");
            return eventRepositoryImpl.viewSingleEvent(id, empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage viewEventLeaderboard(Integer id) throws IOException {
        log.debug("Inside dashboardData (Service) with parametes {} ", id);
        try {
            return eventRepositoryImpl.viewEventLeaderboard(id);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override

    public List<Employee> getManagers() {
        try {
            return eventRepositoryImpl.getManagers();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<Employee> getManagerEmployee(String rmId) {
        try {
            return eventRepositoryImpl.getManagerEmployee(rmId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer addEventAsDraft(Event eventDetails) {
        log.debug("Inside addEventAsDraft() (Service) with parameters {}", eventDetails);
        try {
            return eventRepositoryImpl.addEventAsDraft(eventDetails);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteEvent(Integer id) throws IOException {
        log.debug("Inside deleteEvent() (Service) with parameters {}", id);
        try {
            return eventRepositoryImpl.deleteEvent(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage enroll(String data) {
        log.info("Inside enroll (Service) with parameters {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int eventId = jsonObject.getInt("eventId");
            String empId = jsonObject.getString("empId");
            ResponseMessage responseMessage = eventRepositoryImpl.enroll(eventId, empId);
            if (responseMessage.getMessage().length() < 15) {
                emailService.sendEnrolledEmail(empId, eventId);
            }
            return responseMessage;
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage enrollConformation(String data) {
        log.info("Inside enrollConformation (Service) with parameters {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int eventId = jsonObject.getInt("eventId");
            String empId = jsonObject.getString("empId");
            emailService.sendEnrolledEmail(empId, eventId);
            return eventRepositoryImpl.enrollConformation(eventId, empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage winnerList(Integer id) throws IOException {
        log.debug("Inside winnerList (Service) with parametes{} ", id);
        try {
            return eventRepositoryImpl.winnerList(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
