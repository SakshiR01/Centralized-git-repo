package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface ClientFeedbackService {

    Integer addClientFeedback(Contributions contributions, AttachmentId attachment) throws IOException;

    ResponseMessage previousEvaluation(String data) throws IOException;

    Contributions fetchSingleEvaluation(int feedbackId) throws IOException;

    Integer saveAsADraft(Contributions feedbackDetails, AttachmentId attachment) throws IOException;

    byte[] downloadAttachment(int id) throws IOException;

    String deleteClientFeedback(int contributionId) throws IOException;
}
