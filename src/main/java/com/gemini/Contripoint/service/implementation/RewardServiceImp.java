package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.RewardRepositoryImpl;
import com.gemini.Contripoint.service.interfaces.EmailService;
import com.gemini.Contripoint.service.interfaces.RewardService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RewardServiceImp implements RewardService {

    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);

    @Autowired
    private RewardRepositoryImpl rewardRepositoryImpl;

    @Autowired
    private EmailService emailService;

    @Override
    public ResponseMessage viewSingleReward(String data) throws IOException {
        log.debug("Inside viewSingleReward() (Service) with parameters {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int eventId = jsonObject.getInt("eventId");
            String empId = jsonObject.getString("empId");
            return rewardRepositoryImpl.viewSingleReward(eventId, empId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllReward(String data) throws IOException {
        log.debug("Inside viewSingleEvent (Service) with parameters {} ", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            int pageNo = jsonObject.getInt("pageNo");
            String empId = jsonObject.getString("empId");
            pageNo--;
            return rewardRepositoryImpl.getAllReward(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
