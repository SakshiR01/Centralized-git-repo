package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.AttachmentFile;
import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.WrapperModel;
import com.gemini.Contripoint.service.interfaces.CertificateService;
import com.gemini.Contripoint.service.interfaces.ClientFeedbackService;
import com.gemini.Contripoint.service.interfaces.InterviewService;
import com.gemini.Contripoint.service.interfaces.WrapperModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class WrapperModelServiceImp implements WrapperModelService {

    public static final Logger log = LoggerFactory.getLogger(ClientFeedbackServiceImp.class);

    @Autowired
    private ClientFeedbackService clientFeedbackService;

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private InterviewService interviewService;

    @Override
    public Integer addClientFeedback(WrapperModel feedbackDetails) throws IOException {
        log.info("Inside addClientFeedback (Wrapper Model Service) with parameters {}", feedbackDetails);
        try {
            Contributions contributions;
            AttachmentId attachment = new AttachmentId();    // Generating attachmentId
            AttachmentFile attachmentFile = new AttachmentFile();    // Generating Attachment file
            ObjectMapper objectMapper = new ObjectMapper();
            contributions = objectMapper.readValue(feedbackDetails.getFormDetails(), Contributions.class);  // Mapping data to contribution class
            if (feedbackDetails.getFile() != null) {    // checking for null file
                attachment.setName(feedbackDetails.getFile().getOriginalFilename());
                if (attachment.getName() != "") {    // checking for optional file
                    String extension = StringUtils.getFilenameExtension(attachment.getName());  // getting extension
                    extension = extension.toLowerCase();
                    if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("eml")) {    // handing corrent extension file
                        attachmentFile.setFile(feedbackDetails.getFile().getBytes());
                        attachment.setAttachmentFile(attachmentFile);
                    } else
                        throw new ContripointException("upload only .png, .jpeg, .jpg .eml files", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
                    return clientFeedbackService.addClientFeedback(contributions, attachment);
                } else {
                    return clientFeedbackService.addClientFeedback(contributions, null);
                }
            } else {
                return clientFeedbackService.addClientFeedback(contributions, null);
            }
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveAsADraft(WrapperModel feedbackDetails) throws IOException {
        log.debug("Inside saveAsADraft (Service) with parameters {}", feedbackDetails);
        try {
            Contributions contributions;
            AttachmentId attachment = new AttachmentId();    // Generating attachmentId
            AttachmentFile attachmentFile = new AttachmentFile();    // Generating Attachment file
            ObjectMapper objectMapper = new ObjectMapper();
            contributions = objectMapper.readValue(feedbackDetails.getFormDetails(), Contributions.class);  // Mapping data to contribution class
            if (feedbackDetails.getFile() != null) {    // checking for null file
                attachment.setName(feedbackDetails.getFile().getOriginalFilename());
                if (attachment.getName() != "") {    // checking for optional file
                    String extension = StringUtils.getFilenameExtension(attachment.getName());  // getting extension
                    extension = extension.toLowerCase();
                    if (extension.equals("png") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("eml")) {    // handing corrent extension file
                        attachmentFile.setFile(feedbackDetails.getFile().getBytes());
                        attachment.setAttachmentFile(attachmentFile);
                    } else
                        throw new ContripointException("upload only .png, .jpeg, .jpg .eml files", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
                    return clientFeedbackService.saveAsADraft(contributions, attachment);
                } else {
                    return clientFeedbackService.saveAsADraft(contributions, null);
                }
            } else {
                return clientFeedbackService.saveAsADraft(contributions, null);
            }
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer addCertificate(WrapperModel certificateDetails) throws IOException {
        log.info("Inside addCertificate (Service) with parameters {}", certificateDetails);
        try {
            Contributions contributions;
            AttachmentId attachment = new AttachmentId();    // Setting up AttachmetId
            AttachmentFile attachmentFile = new AttachmentFile();       // Generating AttachmentFile
            ObjectMapper objectMapper = new ObjectMapper();
            contributions = objectMapper.readValue(certificateDetails.getFormDetails(), Contributions.class);       // mapping details with contribution class.
            if (certificateDetails.getFile() != null) {
                attachment.setName(certificateDetails.getFile().getOriginalFilename()); // Setting name of attachment
                String extension = StringUtils.getFilenameExtension(attachment.getName());   // Setting extension
                extension = extension.toLowerCase();
                if (extension.equals("pdf") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("eml")) {
                    attachmentFile.setFile(certificateDetails.getFile().getBytes());
                    attachment.setAttachmentFile(attachmentFile);
                } else
                    throw new ContripointException("upload only .pdf, .jpeg, .jpg .eml files", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);  // handling exception
            }
            return certificateService.addCertificate(contributions, attachment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveasDraft(WrapperModel certificateDetails) throws IOException {
        try {
            Contributions contributions;
            AttachmentId attachment = new AttachmentId();    // Setting up AttachmentId
            AttachmentFile attachmentFile = new AttachmentFile();       // Generating AttachmentFile
            ObjectMapper objectMapper = new ObjectMapper();
            contributions = objectMapper.readValue(certificateDetails.getFormDetails(), Contributions.class);       // mapping details with contribution class.
            if (certificateDetails.getFile() != null) {
                attachment.setName(certificateDetails.getFile().getOriginalFilename()); // Setting name of attachment
                String extension = StringUtils.getFilenameExtension(attachment.getName());   // Setting extension
                extension = extension.toLowerCase();
                if (extension.equals("pdf") || extension.equals("jpeg") || extension.equals("jpg") || extension.equals("eml")) {
                    attachmentFile.setFile(certificateDetails.getFile().getBytes());
                    attachment.setAttachmentFile(attachmentFile);
                } else
                    throw new ContripointException("upload only .pdf, .jpeg, .jpg .eml files", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);  // handling exception
            }
            return certificateService.saveAsDraft(contributions, attachment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
