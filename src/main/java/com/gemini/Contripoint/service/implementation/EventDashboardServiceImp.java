package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.EventDashboardRepositoryImp;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.EventDashboardService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EventDashboardServiceImp implements EventDashboardService {

    public static final Logger log = LoggerFactory.getLogger(EventDashboardServiceImp.class);


    @Autowired
    EventDashboardRepositoryImp eventDashboardRepositoryImp;

    @Autowired
    EmailService emailService;

    @Override
    public ResponseMessage viewUpcomingEvent(String data) {
        log.debug("Inside viewClosedEvent (service) with parameters {}", data);

        try {
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");

            return eventDashboardRepositoryImp.viewUpcomingEvent(pageNo, empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage viewClosedEvent(String data) {
        log.debug("Inside viewClosedEvent (service) with parameters {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");

            return eventDashboardRepositoryImp.viewClosedEvent(pageNo, empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage myEventActivity(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            // Parsing employeeId and pageNo from JSON data
            String employeeId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            // pageNo--;
            return eventDashboardRepositoryImp.myEventActivity(employeeId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage reviewEvent(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            // Parsing employeeId and pageNo from JSON data
            String adminManagerId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            //pageNo--;
            return eventDashboardRepositoryImp.reviewEvent(adminManagerId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String changeStatus(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            int id = jsonObject.getInt("id");
            String type = jsonObject.getString("type");
            if (type.equalsIgnoreCase("Approved")) {
                String result = emailService.eventInvitation(id); // async
                String result1 = emailService.rmApprovedEmail(id); //async
                return eventDashboardRepositoryImp.changeStatus(id, type);
            } else {
                if (type.equalsIgnoreCase("Declined")) {
                    String result2 = emailService.rmDeclinedEmail(id); //async
                    return eventDashboardRepositoryImp.changeStatus(id, type);
                }
            }
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return "";
    }

    @Override
    public String startEndEvent() {
        try {
            return eventDashboardRepositoryImp.startEndEvent();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
