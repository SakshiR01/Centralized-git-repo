package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface InterviewService {

    Integer addInterview(String interviewDetials) throws IOException;

    Integer saveasDraftInterview(String interviewDetails) throws IOException;

    ResponseMessage getAllInterview(String data) throws IOException;

    Contributions fetchSingleInterview(int interviewId) throws IOException;

    List<String> getProfile() throws IOException;

    List<String> getExperience();

    List<String> getMonth();

    String deleteInterview(int id) throws IOException;
}
