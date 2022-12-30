package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ProjectService {

    Integer addProject(String projectDetails) throws IOException;

    Integer saveasDraftProject(String data) throws IOException;

    ResponseMessage getAllProject(String data);

    Contributions fetchSingleProject(int interviewId) throws IOException;

    String deleteProject(int projectId) throws IOException;
}
