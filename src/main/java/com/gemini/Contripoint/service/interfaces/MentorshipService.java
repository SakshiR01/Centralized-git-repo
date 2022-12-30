package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface MentorshipService {

    Integer addMentorship(String mentorshipDetails) throws IOException;

    Integer saveasDraftMentorship(String mentorshipDetails) throws IOException;

    ResponseMessage getAllMentorship(String data);

    Contributions fetchSingleMentorship(int mentorshipId) throws IOException;

    ResponseMessage fetchEmployeeDropDown() throws IOException;

    List<String> fetchTechnologies() throws IOException;

    String deleteMentorship(int id) throws IOException;
}
