package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.ClientFeedbackRepositoryImp;
import com.gemini.Contripoint.service.interfaces.ClientFeedbackService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class ClientFeedbackServiceImp implements ClientFeedbackService {

    public static final Logger log = LoggerFactory.getLogger(ClientFeedbackServiceImp.class);

    @Autowired
    private ClientFeedbackRepositoryImp clientFeedbackRepositoryImp;

    @Override
    public Integer addClientFeedback(Contributions contributions, AttachmentId attachment) throws IOException {
        log.info("Inside addClientFeedback (Service) with parameters {} {}", contributions, attachment);
        try {
            return clientFeedbackRepositoryImp.addClientFeedback(contributions, attachment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage previousEvaluation(String data) throws IOException {
        log.debug("Inside previousEvaluation (Service) with parameters  {}", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return clientFeedbackRepositoryImp.previousEvaluation(empId, pageNo);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions fetchSingleEvaluation(int feedbackId) throws IOException {
        log.debug("Inside fetchSingleEvaluation (Service) with parameters {}", feedbackId);
        try {
            return clientFeedbackRepositoryImp.fetchSingleEvaluation(feedbackId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveAsADraft(Contributions feedbackDetails, AttachmentId attachment) throws IOException {
        log.debug("Inside saveAsADraft (Service) with parameters {} {}", feedbackDetails, attachment);
        try {
            return clientFeedbackRepositoryImp.saveAsADraft(feedbackDetails, attachment);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] downloadAttachment(int id) throws IOException {
        log.debug("Inside downloadAttachment (Service) with parameter{}", id);
        try {
            Contributions contributions = clientFeedbackRepositoryImp.fetchSingleEvaluation(id);
            return contributions.getAttachmentId().getAttachmentFile().getFile();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public String deleteClientFeedback(int contributionId) throws IOException {
        log.debug("Inside deleteClientFeedback (Service) with parameter{}", contributionId);
        try {
            return clientFeedbackRepositoryImp.deleteClientFeedback(contributionId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
