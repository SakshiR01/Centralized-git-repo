package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.enumeration.Active;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.Notification;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.ContributionCategoryRepository;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.repository.interfaces.TechnologiesRepository;
import com.gemini.Contripoint.repository.interfaces.TrainingSessionTechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Component
public class TrainingSessionRepositoryImp {
    public static final Logger log = LoggerFactory.getLogger(TrainingSessionRepositoryImp.class);
    @Autowired
    TrainingSessionTechnologyRepository trainingSessionTechnologyRepository;
    @Autowired
    TechnologiesRepository technologiesRepository;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;
    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;
    @Autowired
    private ContributionsRepository contributionsRepository;

    public int addTrainingSession(Contributions training) throws IOException {
        log.debug("Inside addTrainingSession (Repository) with parameters {}", training);
        String name = employeeRepositoryImp.getEmployeeName(training.getEmpId().getId());   // Getting employee name
        Timestamp time = Timestamp.from(Instant.now());     // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");       // creating date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Creating indian date

        String date = dateFormat.format(time);
        training.setCreatedOn(date); // Setting createdON
        training.setLastModifiedOn(date);// Setting LastModifiedOn
        training.setCreatedBy(name);    // Setting createdBy
        training.setLastModifiedBy(name);   // Setting lastModifiedBy
        if (training.getCount() == 0) { // Setting count as 1 for 0
            training.setCount(1);
        }
        training.setStatus(Status.PENDING.getName()); // Setting status as Pending
        training.setContributionCategory(contributionCategoryRepository.getById(4)); // Setting contribution category as Training and Session
        training.setTotalPoints(training.getCount() * training.getContributionCategory().getPoints());  // Setting points * count
        training.setEmpId(employeeRepositoryImp.getByEmployeeId(training.getEmpId().getId()));
        training.setActive(Active.ACTIVE);   // Setting state as ACTIVE
        // Adding Notifications
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(training.getContributionCategory().getName());
        notification.setEmpId(training.getEmpId().getId());
        training.setNotification(notification);
        // Saving ito the database
        contributionsRepository.save(training);
        return training.getId();
    }

    public Integer saveTrainingSessionAsDraft(Contributions training) throws IOException {
        log.debug("Inside addTrainingSession (Repository) with parameters {}", training);
        String name = employeeRepositoryImp.getEmployeeName(training.getEmpId().getId());   // Getting employee name
        Timestamp time = Timestamp.from(Instant.now());     // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");       // creating date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Creating indian date

        String date = dateFormat.format(time);
        training.setCreatedOn(date); // Setting createdOn
        training.setLastModifiedOn(date);// Setting LastModifiedOn
        training.setCreatedBy(name);    // Setting createdBy
        training.setLastModifiedBy(name);   // Setting lastModifiedBy
        if (training.getCount() == 0) { // Setting count as 1 for 0
            training.setCount(1);
        }
        training.setStatus(Status.DRAFT.getName()); // Setting status as Drafted
        training.setContributionCategory(contributionCategoryRepository.getById(4)); // Setting contribution category as Training and Session
        training.setTotalPoints(training.getCount() * training.getContributionCategory().getPoints());  // Setting points * count
        training.setEmpId(employeeRepositoryImp.getByEmployeeId(training.getEmpId().getId()));
        training.setActive(Active.ACTIVE);   // Setting state as ACTIVE
        // Adding Notifications
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(training.getContributionCategory().getName());
        notification.setEmpId(training.getEmpId().getId());
        training.setNotification(notification);
        // Saving ito the database
        contributionsRepository.save(training);
        return training.getId();
    }

    public ResponseMessage getTrainingSession(String empId, int pageNo) throws IOException {
        log.debug("Inside getTrainingSession (Repository) with parameters {} {}", empId, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5); // Setting page size as 5
        Page<Contributions> trainingSession = contributionsRepository.findAllByEmployeeId(empId, 4, pageable); // Getting data for specific page
        long totalRows = trainingSession.getTotalElements();
        if (trainingSession.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Training Session found", arr, 0);     // handing null case
        }
        List<Contributions> trainingSessionList = trainingSession.stream().map(training -> {
            // Reducing Payload
            training.setLastModifiedBy(null);
            training.setCreatedBy(null);
            training.setLastModifiedBy(null);
            training.setEmpId(null);
            return training;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, trainingSessionList, totalRows); // returning data as Response Message
    }

    public Contributions getSingleTrainingSession(Integer id) throws IOException {
        log.debug("Inside getSingleTrainingSession (Repository) with parameters {}", id);
        Contributions trainingSession;
        trainingSession = contributionsRepository.findById(id).orElse(null);     // getting data of a specific training session
        // reducing payload
        trainingSession.setLastModifiedBy(null);
        trainingSession.setEmpId(null);
        return trainingSession;
    }

    public String deleteTrainingSession(int id) {

        log.debug("Inside deleteTrainingSession() with  parameters {}", id);
        Contributions contributions = contributionsRepository.findById(id).orElse(null);
        int trainingAndSessionId = contributions.getTrainingAndSession().getId();
        List<Integer> ids = trainingSessionTechnologyRepository.findTechnologyId(trainingAndSessionId);
        trainingSessionTechnologyRepository.deleteTrainingSessionTechnology(trainingAndSessionId);
        contributionsRepository.deleteContribution(id);
        trainingSessionTechnologyRepository.deleteByyId(trainingAndSessionId);
        for (int i = 0; i < ids.size(); i++) {
            technologiesRepository.deleteTechnology(ids.get(i));
        }
        return "deleted trainingSession";
    }

}
