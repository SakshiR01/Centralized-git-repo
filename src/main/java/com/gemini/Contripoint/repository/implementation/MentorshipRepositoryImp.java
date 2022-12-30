package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.enumeration.Active;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.Employee;
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
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class MentorshipRepositoryImp {
    public static final Logger log = LoggerFactory.getLogger(InterviewRepositoryImp.class);
    @Autowired
    TechnologyRepositoryImp technologyRepositoryImp;
    @Autowired
    ContributionsRelatedEmployeeRepository contributionsRelatedEmployeeRepository;
    @Autowired
    RelatedEmployeeTechnologyRepository relatedEmployeeTechnologyRepository;
    @Autowired
    TechnologiesRepository technologiesRepository;
    @Autowired
    private ContributionsRepository contributionsRepository;
    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    public Integer addMentorship(Contributions mentorship) throws IOException {
        log.debug("Inside addMentorship (Repository) with parameters {} ", mentorship);
        AtomicInteger i = new AtomicInteger();  // Creating an integer as i
        String name = employeeRepositoryImp.getEmployeeName(mentorship.getEmpId().getId());     //  Getting the employee name
        mentorship.getRelatedEmployee().forEach(relatedEmployee -> {
            relatedEmployee.setName(employeeRepositoryImp.getEmployeeName(relatedEmployee.getEmpId())); // Getting the employee's mentored
            i.getAndIncrement();     // incrementing the value of i
        });
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Getting the date and time
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Getting date and time as in india

        String date = dateFormat.format(time);
        mentorship.setCreatedOn(date);   // Setting created On
        mentorship.setCreatedBy(name);   // Setting Created BY
        mentorship.setLastModifiedOn(date);      // Setting lastModified On
        mentorship.setCount(Integer.parseInt(i.toString()));    // Setting count as i
        mentorship.setLastModifiedBy(name);  // Setting LastModifiedBy as name
        mentorship.setStatus(Status.PENDING.getName());     // Setting status as pending
        mentorship.setContributionCategory(contributionCategoryRepository.getById(5));  // Setting the contribution Category
        mentorship.setTotalPoints(mentorship.getCount() * mentorship.getContributionCategory().getPoints()); // points*count
        mentorship.setEmpId(employeeRepositoryImp.getByEmployeeId(mentorship.getEmpId().getId()));  // Setting employee id
        mentorship.setActive(Active.ACTIVE);    // Setting state as Active
        // Setting notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(mentorship.getContributionCategory().getName());
        notification.setEmpId(mentorship.getEmpId().getId());
        // Setting up the notification
        mentorship.setNotification(notification);
        // Saving into the database
        contributionsRepository.save(mentorship);
        return mentorship.getId();
    }

    public Integer saveasDraftMentorship(Contributions mentorship) throws IOException {
        log.debug("Inside saveasDraftMentorship() with parameters {} ", mentorship);
        AtomicInteger i = new AtomicInteger();  // Creating an integer as i
        String name = employeeRepositoryImp.getEmployeeName(mentorship.getEmpId().getId());     //  Getting the employee name
        mentorship.getRelatedEmployee().forEach(relatedEmployee -> {
            relatedEmployee.setName(employeeRepositoryImp.getEmployeeName(relatedEmployee.getEmpId())); // Getting the employee's mentored
            i.getAndIncrement();     // incrementing the value of i
        });
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Getting the date and time
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Getting date and time as in india

        String date = dateFormat.format(time);
        mentorship.setCreatedOn(date);   // Setting created On
        mentorship.setCreatedBy(name);   // Setting Created BY
        mentorship.setLastModifiedOn(date);      // Setting lastModified On
        mentorship.setCount(Integer.parseInt(i.toString()));    // Setting count as i
        mentorship.setLastModifiedBy(name);  // Setting LastModifiedBy as name
        mentorship.setStatus(Status.DRAFT.getName());     // Setting status as Drafted
        mentorship.setContributionCategory(contributionCategoryRepository.getById(5));  // Setting the contribution Category
        mentorship.setTotalPoints(mentorship.getCount() * mentorship.getContributionCategory().getPoints()); // points*count
        mentorship.setEmpId(employeeRepositoryImp.getByEmployeeId(mentorship.getEmpId().getId()));  // Setting employee id
        mentorship.setActive(Active.ACTIVE);    // Setting state as Active
        // Setting notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(mentorship.getContributionCategory().getName());
        notification.setEmpId(mentorship.getEmpId().getId());
        // Setting up the notification
        mentorship.setNotification(notification);
        // Saving into the database
        contributionsRepository.save(mentorship);
        return mentorship.getId();
    }

    public ResponseMessage getAllMentorship(String id, int pageNo) {
        log.debug("Inside getAllMentorship() with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);      // Setting pagesize as 5
        Page<Contributions> mentorships = contributionsRepository.findAllByEmployeeId(id, 5, pageable); // Getting the data on a specific page
        long totalRows = mentorships.getTotalElements();
        if (mentorships.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Mentorship found", arr, 0); // handling the empty database
        }
        List<Contributions> mentorshipList = mentorships.stream().map(mentorship -> {
            // Reducing payload
            mentorship.setLastModifiedBy(null);
            mentorship.setCreatedBy(null);
            mentorship.setLastModifiedOn(null);
            mentorship.setLastModifiedBy(null);
            mentorship.setEmpId(null);
            return mentorship;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, mentorshipList, totalRows);
    }

    public Contributions fetchSingleMentorship(int mentorshipId) throws IOException {
        log.debug("Inside fetchSingleMentorship() with parameters {} ", mentorshipId);
        Contributions interview;
        interview = contributionsRepository.findById(mentorshipId).orElse(null); // getting single interview
        // Reducing the payload
        interview.setEmpId(null);
        interview.setLastModifiedBy(null);
        interview.setLastModifiedOn(null);
        if (interview == null) {
            throw new ContripointException("No such Interview exists", HttpStatus.NOT_FOUND); // handling the null case
        }
        return interview;
    }

    public ResponseMessage fetchEmployeeDropDown() throws IOException {
        log.debug("Inside fetchEmployeeDropDown() with no parameters");
        List<Employee> employeeList = employeeRepositoryImp.getEmployeeList(); // Fetching the employee Drop down
        List<Employee> employeeList1 = employeeList.stream().map(employee -> {
            // Reducing the payload.
            employee.setDesignation(null);
            employee.setImage(null);
            employee.setRmId(null);
            employee.setTeam(null);
            return employee;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, employeeList1);
    }


    public List<String> fetchTechnologies() {
        log.debug("Inside fetchTechnologies() with no parameters");
        List<String> technologies = technologyRepositoryImp.getTechnologyList(); // fetching the list from database.
        technologies.add("Other");
        return technologies;
    }

    public String deleteMentorship(int id) throws IOException {
        log.debug("Inside deleteMentorship() with  parameters {}", id);
        List<Integer> techIds = new ArrayList<>();
        Contributions contributions = contributionsRepository.findById(id).orElse(null);
        List<Integer> ids = contributionsRelatedEmployeeRepository.findRelatedEmployee(id);
        for (int i = 0; i < ids.size(); i++) {
            // techIds.addAll(relatedEmployeeTechnologyRepository.findTechnologyId(ids.get(i)));

            List<Integer> l = relatedEmployeeTechnologyRepository.findTechnologyId(ids.get(i));
            l.forEach(j -> {
                techIds.add(j);
            });
        }
        for (int i = 0; i < ids.size(); i++) {
            relatedEmployeeTechnologyRepository.deleteTechnologyId(ids.get(i));
        }
        contributionsRelatedEmployeeRepository.deleteRelatedEmployee(id);
        for (int i = 0; i < ids.size(); i++) {
            relatedEmployeeTechnologyRepository.deleteByyId(ids.get(i));
        }
        contributionsRepository.deleteContribution(id);
        for (int j = 0; j < techIds.size(); j++) {
            technologiesRepository.deleteById(techIds.get(j));
        }

        return "deleted mentorship";
    }
}
