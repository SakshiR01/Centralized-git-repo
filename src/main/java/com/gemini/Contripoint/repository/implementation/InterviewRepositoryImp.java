package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.enumeration.Active;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.Notification;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.ContributionCategoryRepository;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.repository.interfaces.InterviewProfileRepository;
import com.gemini.Contripoint.repository.interfaces.ProfilesRepository;
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
public class InterviewRepositoryImp {
    public static final Logger log = LoggerFactory.getLogger(InterviewRepositoryImp.class);
    @Autowired
    InterviewProfileRepository interviewProfileRepository;
    @Autowired
    ProfilesRepository profilesRepository;
    @Autowired
    private ContributionsRepository contributionsRepository;
    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;
    @Autowired
    private ProfileRepositoryImp profileRepositoryImp;
    @Autowired
    private ExperienceRepositoryImp experienceRepositoryImp;
    @Autowired
    private MonthRepositoryImp monthRepositoryImp;

    public Integer addInterview(Contributions interview) throws IOException {
        log.debug("Inside addInterview (Repository) with parameters {} ", interview);
        String name = employeeRepositoryImp.getEmployeeName(interview.getEmpId().getId());
        if (interview.getCount() == 0) {    // if count is 0 set it as 1 (default)
            interview.setCount(1);
        }
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Converting time to IST

        String date = dateFormat.format(time);
        interview.setCreatedOn(date);   // setting created on
        interview.setCreatedBy(name);    // setting created by
        interview.setLastModifiedOn(date);   // setting lastModified on
        interview.setLastModifiedBy(name);   // Setting lastModified by
        interview.setStatus(Status.PENDING.getName());  // Setting status as pending
        interview.setContributionCategory(contributionCategoryRepository.getById(3)); // Linking with contribution Category and setting category as 3
        interview.setTotalPoints(interview.getCount() * interview.getContributionCategory().getPoints());   // setting count * points
        interview.setEmpId(employeeRepositoryImp.getByEmployeeId(interview.getEmpId().getId())); // Setting employee Id
        interview.setActive(Active.ACTIVE); // Setting Status as Active
        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(interview.getContributionCategory().getName());
        notification.setEmpId(interview.getEmpId().getId());
        interview.setNotification(notification);
        // Saving into database
        contributionsRepository.save(interview);
        return interview.getId();
    }

    public Integer saveasDraftInterview(Contributions interview) throws IOException {
        log.debug("Inside saveasDraftInterview (Repository) with parameters {} ", interview);
        String name = employeeRepositoryImp.getEmployeeName(interview.getEmpId().getId());
        if (interview.getCount() == 0) {    // if count is 0 set it as 1 (default)
            interview.setCount(1);
        }
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Converting time to IST

        String date = dateFormat.format(time);
        interview.setCreatedOn(date);   // setting created on
        interview.setCreatedBy(name);    // setting created by
        interview.setLastModifiedOn(date);   // setting lastModified on
        interview.setLastModifiedBy(name);   // Setting lastModified by
        interview.setStatus(Status.DRAFT.getName());  // Setting status as Draft
        interview.setContributionCategory(contributionCategoryRepository.getById(3)); // Linking with contribution Category and setting category as 3
        interview.setTotalPoints(interview.getCount() * interview.getContributionCategory().getPoints());   // setting count * points
        interview.setEmpId(employeeRepositoryImp.getByEmployeeId(interview.getEmpId().getId())); // Setting employee Id
        interview.setActive(Active.ACTIVE); // Setting Status as Active
        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(interview.getContributionCategory().getName());
        notification.setEmpId(interview.getEmpId().getId());
        interview.setNotification(notification);
        // Saving into database
        contributionsRepository.save(interview);
        return interview.getId();
    }

    public ResponseMessage getAllInterview(String id, int pageNo) {
        log.debug("Inside fetchSingleInterview (Repository) with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);
        Page<Contributions> interviews = contributionsRepository.findAllByEmployeeId(id, 3, pageable);   // Fetching data from database
        long totalRows = interviews.getTotalElements();
        if (interviews.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Interviews found", arr, 0); // Sending empty array in case of no interview
        }
        List<Contributions> interviewList = interviews.stream().map(interview -> {
            // Reducing Payload
            interview.setLastModifiedBy(null);
            interview.setCreatedBy(null);
            interview.setLastModifiedOn(null);
            interview.setLastModifiedBy(null);
            interview.setEmpId(null);
            return interview;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, interviewList, totalRows);
    }

    public Contributions fetchSingleEvaluation(int interviewId) throws IOException {
        log.debug("Inside fetchSingleInterview (Repository) with parameters {} ", interviewId);
        Contributions interview;
        interview = contributionsRepository.findById(interviewId).orElse(null); // Getting the data from database
        // Reducing the payload
        interview.setEmpId(null);
        interview.setLastModifiedBy(null);
        interview.setLastModifiedOn(null);
        if (interview == null) {
            throw new ContripointException("No such Interview exists", HttpStatus.NOT_FOUND); // Handling the null case
        }
        return interview;
    }


    public List<String> getProfile() throws IOException {
        log.debug("Inside getProfile (Repository) with no parameters.");
        return profileRepositoryImp.getProfile();   // Getting the profile dropdown
    }

    public List<String> getExperience() {
        log.debug("Inside getExperience with no parameters");
        return experienceRepositoryImp.getExperience();     // Getting the experience Dropdown
    }

    public List<String> getMonth() {
        log.debug("Inside getMonth() with no parameters");
        return monthRepositoryImp.getMonth();   // Getting the month Drop Down
    }

    public String deleteInterview(int id) {
        log.debug("Inside deleteInterview() with  parameters {}", id);
        Contributions contributions = contributionsRepository.findById(id).orElse(null);
        int interviewId = contributions.getInterview().getId();
        List<Integer> ids = interviewProfileRepository.findProfileId(interviewId);
        interviewProfileRepository.deleteInterviewProfile(interviewId);
        contributionsRepository.deleteContribution(id);
        interviewProfileRepository.deleteByyId(interviewId);
        for (int i = 0; i < ids.size(); i++) {
            profilesRepository.deleteByyId(ids.get(i));
        }
        return "deleted interview";
    }

}
