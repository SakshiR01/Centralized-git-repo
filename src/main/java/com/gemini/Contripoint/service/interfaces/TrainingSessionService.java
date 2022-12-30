package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TrainingSessionService {
    Integer addTrainingSession(String formDetails) throws IOException;

    Integer saveTrainingSessionAsDraft(String formDetails) throws IOException;

    ResponseMessage getTrainingSession(String data) throws IOException;

    Contributions getSingleTrainingSession(Integer id) throws IOException;

    String deleteTrainingSession(int id) throws IOException;
}
