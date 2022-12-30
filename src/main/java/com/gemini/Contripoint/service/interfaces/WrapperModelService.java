package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.WrapperModel;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WrapperModelService {
    Integer addClientFeedback(WrapperModel feedbackDetails) throws IOException;

    Integer saveAsADraft(WrapperModel feedbackDetails) throws IOException;

    Integer addCertificate(WrapperModel certificateDetails) throws IOException;

    Integer saveasDraft(WrapperModel certificate) throws IOException;


}
