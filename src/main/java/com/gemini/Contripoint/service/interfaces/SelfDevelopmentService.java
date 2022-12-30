package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface SelfDevelopmentService {
    Integer addSelfDevelopment(String selfDevelopmentDetails) throws IOException;

    Integer saveAsDraftSelfDevelopment(String selfDevelopmentDetails) throws IOException;

    Contributions fetchSingleSelfDevelopment(int id) throws IOException;

    ResponseMessage getAllSelfDevelopment(String data);

    String deleteSelfDevelopment(int id) throws IOException;

}