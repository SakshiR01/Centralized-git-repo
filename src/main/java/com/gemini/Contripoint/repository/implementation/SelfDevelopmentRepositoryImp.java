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
import com.gemini.Contripoint.repository.interfaces.SelfDevelopmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Repository
public class SelfDevelopmentRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(SelfDevelopmentRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;

    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;

    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private SelfDevelopmentRepository selfDevelopmentRepository;

    public Integer addSelfDevelopment(Contributions selfDevelopment) {
        log.debug("Inside addSelfDevelopment (Repository) with parameters {} ", selfDevelopment);

        try {
            String name = employeeRepositoryImp.getEmployeeName(selfDevelopment.getEmpId().getId());    // Getting employee name
            Timestamp time = Timestamp.from(Instant.now());      // Getting current time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");      // Getting current date
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));   // Getting indian date
            String date = dateFormat.format(time);
            selfDevelopment.setCreatedOn(date);     // Setting CreatedOn
            selfDevelopment.setCreatedBy(name);     // Setting createdBy
            selfDevelopment.setLastModifiedOn(date);    // Setting LastModifiedOn
            selfDevelopment.setCount(1);    // Setting default as 1
            selfDevelopment.setLastModifiedBy(name);    // Setting lastModifiedBy
            selfDevelopment.setStatus(Status.PENDING.getName());     // Setting status as Pending
            selfDevelopment.setContributionCategory(contributionCategoryRepository.getById(8));  // setting contribution Category as SelfDevelopment
            selfDevelopment.setTotalPoints(selfDevelopment.getCount() * selfDevelopment.getContributionCategory().getPoints()); // points*count
            selfDevelopment.setEmpId(employeeRepositoryImp.getByEmployeeId(selfDevelopment.getEmpId().getId())); // Setting up employeeID
            // setting up notification
            Notification notification = new Notification();
            notification.setNotificationStatus(NotificationStatus.NEW);
            notification.setContributionCategory(selfDevelopment.getContributionCategory().getName());
            notification.setEmpId(selfDevelopment.getEmpId().getId());
            selfDevelopment.setNotification(notification);
            selfDevelopment.setActive(Active.ACTIVE);   // Setting state as ACTIVE
            // Saving into the database

            contributionsRepository.save(selfDevelopment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return selfDevelopment.getId();
    }

    public Integer saveAsDraftSelfDevelopment(Contributions selfDevelopment) {
        log.debug("Inside saveAsDraftSelfDevelopment (Repository) with parameters {} ", selfDevelopment);

        try {
            String name = employeeRepositoryImp.getEmployeeName(selfDevelopment.getEmpId().getId());    // Getting employee name
            Timestamp time = Timestamp.from(Instant.now());      // Getting current time
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");      // Getting current date
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));   // Getting indian date
            String date = dateFormat.format(time);
            selfDevelopment.setCreatedOn(date);     // Setting CreatedOn
            selfDevelopment.setCreatedBy(name);     // Setting createdBy
            selfDevelopment.setLastModifiedOn(date);    // Setting LastModifiedOn
            selfDevelopment.setCount(1);    // Setting default as 1
            selfDevelopment.setLastModifiedBy(name);    // Setting lastModifiedBy
            selfDevelopment.setStatus(Status.DRAFT.getName());     // Setting status as Drafted
            selfDevelopment.setContributionCategory(contributionCategoryRepository.getById(8));  // setting contribution Category as SelfDevelopment
            selfDevelopment.setTotalPoints(selfDevelopment.getCount() * selfDevelopment.getContributionCategory().getPoints()); // points*count
            selfDevelopment.setEmpId(employeeRepositoryImp.getByEmployeeId(selfDevelopment.getEmpId().getId())); // Setting up employeeID
            // setting up notification
            Notification notification = new Notification();
            notification.setNotificationStatus(NotificationStatus.NEW);
            notification.setContributionCategory(selfDevelopment.getContributionCategory().getName());
            notification.setEmpId(selfDevelopment.getEmpId().getId());
            selfDevelopment.setNotification(notification);
            selfDevelopment.setActive(Active.ACTIVE);   // Setting state as ACTIVE
            // Saving into the database
            contributionsRepository.save(selfDevelopment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return selfDevelopment.getId();

    }

    public ResponseMessage getAllSelfDevelopment(String id, int pageNo) {
        log.debug("Inside getAllSelfDevelopment (Repository) with parameters {} ", id, pageNo);

        try {
            Pageable pageable = PageRequest.of(pageNo, 5);   // Setting page size as 5
            Page<Contributions> selfDevelopment = contributionsRepository.findAllByEmployeeId(id, 8, pageable); // Getting data of specific date
            long totalRows = selfDevelopment.getTotalElements();
            if (selfDevelopment.isEmpty()) {
                int[] arr = new int[0];
                return new ResponseMessage("No Self Development found", arr, 0); // handling the null case
            }
            List<Contributions> selfDevelopmentList = selfDevelopment.stream().map(selfDevelopments -> {
                // Reducing the payload
                selfDevelopments.setLastModifiedBy(null);
                selfDevelopments.setCreatedBy(null);
                selfDevelopments.setLastModifiedOn(null);
                selfDevelopments.setLastModifiedBy(null);
                selfDevelopments.setEmpId(null);
                return selfDevelopments;
            }).collect(Collectors.toList());
            return new ResponseMessage(null, selfDevelopmentList, totalRows);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Contributions fetchSingleSelfDevelopment(int selfDevelopmentId) {
        log.debug("Inside fetchSingleSelfDevelopment() with parameters {} ", selfDevelopmentId);
        try {
            Contributions selfDevelopment;
            selfDevelopment = contributionsRepository.findById(selfDevelopmentId).orElse(null); // Fetching single selfDevelopment
            // Reducing Payload
            selfDevelopment.setEmpId(null);
            selfDevelopment.setLastModifiedBy(null);
            selfDevelopment.setLastModifiedOn(null);
            if (selfDevelopment == null) {
                throw new ContripointException("No such Self Development exists", HttpStatus.NOT_FOUND);
            }
            return selfDevelopment;
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteSelfDevelopment(int selfDevelopmentId) {
        log.debug("Inside deleteSelfDevelopment() with parameters {} ", selfDevelopmentId);
        try {
            Contributions selfDevelopment;
            selfDevelopment = contributionsRepository.findById(selfDevelopmentId).orElse(null);
            if (selfDevelopment == null) {
                throw new ContripointException("No such Self Development exists", HttpStatus.NOT_FOUND);
            }
            int id = selfDevelopment.getSelfDevelopment().getId();
            contributionsRepository.deleteContribution(selfDevelopmentId);
            selfDevelopmentRepository.deleteSelfDevelopment(id);
            return "deleted Self Development";
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}