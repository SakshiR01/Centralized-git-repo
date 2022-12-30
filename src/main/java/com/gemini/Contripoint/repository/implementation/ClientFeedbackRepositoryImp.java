package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.enumeration.Active;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.Notification;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Repository
public class ClientFeedbackRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(ClientFeedbackRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;


    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;

    @Autowired
    private ClientFeedbackRepository clientFeedbackRepository;

    @Autowired
    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    private AttachmentIdRepository attachmentIdRepository;


    public Integer addClientFeedback(Contributions feedback, AttachmentId attachment) throws IOException {
        log.info("Inside addClientFeedbackRepoImp (Repository) with parameters {} {}", feedback, attachment);
        if (attachment != null) {
            feedback.setAttachmentId(attachment);   // if attachment then linking attachment to contribution
        }
        // Setting up contributions Table
        String name = employeeRepositoryImp.getEmployeeName(feedback.getEmpId().getId());   // Fetching name of employee who uploaded the feedback.
        Timestamp time = Timestamp.from(Instant.now()); // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting india time
        String date = dateFormat.format(time);
        feedback.setCreatedOn(date);  // Setting date and time
        feedback.setLastModifiedOn(date); // Setting lastModifiedBy
        feedback.setCreatedBy(name);    // Setting CreatedBy
        feedback.setStatus(Status.PENDING.getName());   // Setting status
        feedback.setCount(1);    // Setting count as 1 (default)
        feedback.setLastModifiedBy(name);
        feedback.setActive(Active.ACTIVE); // Setting state

        // Linking contribution_category to contribution table.
        feedback.setContributionCategory(contributionCategoryRepository.getById(2));
        feedback.setTotalPoints(feedback.getCount() * feedback.getContributionCategory().getPoints());    // setting count*points

        if (feedback.getAttachmentId() != null) {
            if (feedback.getAttachmentId().getAttachmentFile().getFile() == null) {     // if attachment is null look for already uploaded attachment
                Contributions previousFeedback = contributionsRepository.findContributionById(feedback.getId());
                feedback.getAttachmentId().getAttachmentFile().setFile(previousFeedback.getAttachmentId().getAttachmentFile().getFile());
                feedback.getAttachmentId().setName(previousFeedback.getAttachmentId().getName());
            }
        }

        feedback.setEmpId(employeeRepositoryImp.getByEmployeeId(feedback.getEmpId().getId()));  // Setting up employeeId

        // Setting up Notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(feedback.getContributionCategory().getName());
        notification.setEmpId(feedback.getEmpId().getId());
        feedback.setNotification(notification);

        // Saving into database
        contributionsRepository.save(feedback);
        return feedback.getId();
    }

    public ResponseMessage previousEvaluation(String id, int pageNo) throws IOException {
        log.debug("Inside previousEvaluation (Repository) with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);   // Setting up pageable with page size as 5
        Page<Contributions> clientFeedbacks = contributionsRepository.findAllByEmployeeId(id, 2, pageable); // Fetching data from database
        long totalRows = clientFeedbacks.getTotalElements(); // Getting total elements
        if (clientFeedbacks.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Evaluations Found", arr, 0); // Sending empty array in case no records
        }
        List<Contributions> feedbacks = clientFeedbacks.stream().map(feedback -> {
            // Reducing payload
            feedback.setEmpId(null);
            feedback.setCreatedBy(null);
            feedback.setLastModifiedOn(null);
            feedback.setLastModifiedBy(null);
            return feedback;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, feedbacks, totalRows);
    }

    public Contributions fetchSingleEvaluation(int feedbackId) throws IOException {
        log.debug("Inside fetchSingleEvaluation (Repository) with parameters {}", feedbackId);
        Contributions feedbackDetails;
        feedbackDetails = contributionsRepository.findById(feedbackId).orElse(null);    // Getting specific feedback or sending null
        if (feedbackDetails == null)
            throw new ContripointException("No such record exist", HttpStatus.NOT_FOUND);   // Null case handled
        return feedbackDetails;
    }

    public Integer saveAsADraft(Contributions feedback, AttachmentId attachment) throws IOException {
        log.debug("Inside saveAsADraft (Repository) with parameters {} {}", feedback, attachment);
        if (attachment != null) {
            feedback.setAttachmentId(attachment);   // if attachment then linking attachment to contribution
        }
        // Setting up contributions Table
        String name = employeeRepositoryImp.getEmployeeName(feedback.getEmpId().getId());   // Fetching name of employee who uploaded the feedback.
        Timestamp time = Timestamp.from(Instant.now()); // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting india time
        String date = dateFormat.format(time);
        feedback.setCreatedOn(date);  // Setting date and time
        feedback.setLastModifiedOn(date); // Setting lastModifiedBy
        feedback.setCreatedBy(name);    // Setting CreatedBy
        feedback.setStatus(Status.DRAFT.getName());   // Setting status
        feedback.setCount(1);    // Setting count as 1 (default)
        feedback.setLastModifiedBy(name);
        feedback.setActive(Active.ACTIVE); // Setting state

        // Linking contribution_category to contribution table.
        feedback.setContributionCategory(contributionCategoryRepository.getById(2));
        feedback.setTotalPoints(feedback.getCount() * feedback.getContributionCategory().getPoints());    // setting count*points

        if (feedback.getAttachmentId() != null) {
            if (feedback.getAttachmentId().getAttachmentFile().getFile() == null) {     // if attachment is null look for already uploaded attachment
                Contributions previousFeedback = contributionsRepository.findContributionById(feedback.getId());
                feedback.getAttachmentId().getAttachmentFile().setFile(previousFeedback.getAttachmentId().getAttachmentFile().getFile());
                feedback.getAttachmentId().setName(previousFeedback.getAttachmentId().getName());
            }
        }

        feedback.setEmpId(employeeRepositoryImp.getByEmployeeId(feedback.getEmpId().getId()));  // Setting up employeeId

        // Setting up Notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(feedback.getContributionCategory().getName());
        notification.setEmpId(feedback.getEmpId().getId());
        feedback.setNotification(notification);

        // Saving into database
        contributionsRepository.save(feedback);
        return feedback.getId();
    }


    public String deleteClientFeedback(int contributionId) throws IOException {
        log.debug("Inside deleteClientFeedback() Respository with parameters {}", contributionId);
        Contributions clientFeedback;
        clientFeedback = contributionsRepository.findById(contributionId).orElse(null);
        if (clientFeedback == null) {
            throw new ContripointException("client feedback does not exist", HttpStatus.NOT_FOUND);
        }
        int id = clientFeedback.getClientFeedback().getId();
        if (clientFeedback.getAttachmentId() != null) {
            int fileId = clientFeedback.getAttachmentId().getId();
            contributionsRepository.deleteContribution(contributionId);
            clientFeedbackRepository.deleteClientFeedback(id);
            attachmentIdRepository.deleteAttachmentId(fileId);
            attachmentFileRepository.deleteAttachmentFile(fileId);
        } else {
            contributionsRepository.deleteContribution(contributionId);
            clientFeedbackRepository.deleteClientFeedback(id);
        }

        return "Client Feedback Deleted";

    }
}
