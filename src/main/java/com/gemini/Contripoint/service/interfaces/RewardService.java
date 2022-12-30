package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public interface RewardService {
    ResponseMessage viewSingleReward(String data) throws IOException;

    ResponseMessage getAllReward(String data) throws IOException;

}
