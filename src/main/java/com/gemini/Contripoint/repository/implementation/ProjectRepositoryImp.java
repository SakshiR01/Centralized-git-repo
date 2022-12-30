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
import com.gemini.Contripoint.repository.interfaces.ProjectRepository;
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
public class ProjectRepositoryImp {
    public static final Logger log = LoggerFactory.getLogger(InterviewRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;

    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;

    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private ProjectRepository projectRepository;

    public Integer addProject(Contributions projectDetails) throws IOException {
        log.debug("Inside addProject (Repository) with parameters {} ", projectDetails);
        String name = employeeRepositoryImp.getEmployeeName(projectDetails.getEmpId().getId()); // Getting employee name
        Timestamp time = Timestamp.from(Instant.now());  // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Getting dateFormat
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Getting india time

        String date = dateFormat.format(time);
        projectDetails.setCreatedOn(date);   // Setting createdOn
        projectDetails.setCreatedBy(name);  // Setting CreatedBy
        projectDetails.setLastModifiedOn(date);     // Setting lastmodifiedOn
        projectDetails.setCount(1);     // Setting count default
        projectDetails.setLastModifiedBy(name); // Setting lastModifiedBy
        projectDetails.setStatus(Status.PENDING.getName());     // Setting status as Pending
        projectDetails.setActive(Active.ACTIVE);     // Setting state as Active

        projectDetails.setContributionCategory(contributionCategoryRepository.getById(6));      // Setting contribution category as project
        projectDetails.setTotalPoints(projectDetails.getCount() * projectDetails.getContributionCategory().getPoints());    // setting count*points
        projectDetails.setEmpId(employeeRepositoryImp.getByEmployeeId(projectDetails.getEmpId().getId()));
        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(projectDetails.getContributionCategory().getName());
        notification.setEmpId(projectDetails.getEmpId().getId());
        projectDetails.setNotification(notification);
        // Saving into the database
        contributionsRepository.save(projectDetails);

        return projectDetails.getId();
    }

    public Integer saveasDraftProject(Contributions projectDetails) throws IOException {
        log.debug("Inside addProject (Repository) with parameters {} ", projectDetails);
        String name = employeeRepositoryImp.getEmployeeName(projectDetails.getEmpId().getId()); // Getting employee name
        Timestamp time = Timestamp.from(Instant.now());  // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Getting dateFormat
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));    // Getting india time

        String date = dateFormat.format(time);
        projectDetails.setCreatedOn(date);   // Setting createdOn
        projectDetails.setCreatedBy(name);  // Setting CreatedBy
        projectDetails.setLastModifiedOn(date);     // Setting lastmodifiedOn
        projectDetails.setCount(1);     // Setting count default
        projectDetails.setLastModifiedBy(name); // Setting lastModifiedBy
        projectDetails.setStatus(Status.DRAFT.getName());     // Setting status as Pending
        projectDetails.setActive(Active.ACTIVE);     // Setting state as Active

        projectDetails.setContributionCategory(contributionCategoryRepository.getById(6));      // Setting contribution cateogy as project
        projectDetails.setTotalPoints(projectDetails.getCount() * projectDetails.getContributionCategory().getPoints());    // setting count*points
        projectDetails.setEmpId(employeeRepositoryImp.getByEmployeeId(projectDetails.getEmpId().getId()));
        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(projectDetails.getContributionCategory().getName());
        notification.setEmpId(projectDetails.getEmpId().getId());
        projectDetails.setNotification(notification);
        // Saving into the database
        contributionsRepository.save(projectDetails);

        return projectDetails.getId();
    }

    public ResponseMessage getAllProject(String id, int pageNo) {
        log.debug("Inside getAllProject() with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5); // Setting page size as 5
        Page<Contributions> projects = contributionsRepository.findAllByEmployeeId(id, 6, pageable); // Getting the data from database of specific page
        long totalRows = projects.getTotalElements();

        if (projects.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Projects found", arr, 0); // handling the empty case
        }
        List<Contributions> projectList = projects.stream().map(project -> {
            // Reducing the payload
            project.setLastModifiedBy(null);
            project.setCreatedBy(null);
            project.setLastModifiedOn(null);
            project.setLastModifiedBy(null);
            project.setEmpId(null);
            return project;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, projectList, totalRows);
    }

    public Contributions fetchSingleProject(int projectId) throws IOException {
        log.debug("Inside fetchSingleProject() with parameters {} ", projectId);
        Contributions project;
        project = contributionsRepository.findById(projectId).orElse(null); // Fetching single project
        // Reducing the payload
        project.setEmpId(null);
        project.setLastModifiedBy(null);
        project.setLastModifiedOn(null);
        if (project == null) {
            throw new ContripointException("No such Project exists", HttpStatus.NOT_FOUND); // handling the null case
        }
        return project;
    }

    public String deleteProject(int projectId) throws IOException {
        log.debug("Inside deleteProject() repo with parameters {} ", projectId);
        try {
            Contributions project;
            project = contributionsRepository.findById(projectId).orElse(null);
            if (project == null) {
                throw new ContripointException("No such Project exists lol", HttpStatus.NOT_FOUND);
            }
            int proId = project.getProject().getId();
            contributionsRepository.deleteContribution(projectId);
            projectRepository.deleteProject(proId);
            return "Project deleted";
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
