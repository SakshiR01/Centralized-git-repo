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
import com.gemini.Contripoint.repository.interfaces.TeamBuildingRepository;
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
public class TeamBuildingRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(TeamBuildingRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;

    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;


    @Autowired
    private TeamBuildingRepository teamBuildingRepository;

    public Integer addTeamBuilding(Contributions teamBuildingDetails) throws IOException {
        log.debug("Inside addTeamBuilding (Repository) with parameters {}", teamBuildingDetails);
        String name = employeeRepositoryImp.getEmployeeName(teamBuildingDetails.getEmpId().getId()); // Getting name
        teamBuildingDetails.setCount(1);// Setting count as 1 default
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Creating date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian time
        String date = dateFormat.format(time);
        teamBuildingDetails.setCreatedOn(date);  // Setting CreatedOn;
        teamBuildingDetails.setCreatedBy(name);     // Setting cratedBy
        teamBuildingDetails.setLastModifiedOn(date); // Setting LastModifiedOn
        teamBuildingDetails.setLastModifiedBy(name);    // Setting LastModifiedBy
        teamBuildingDetails.setStatus(Status.PENDING.getName());     // Setting status as Pending
        teamBuildingDetails.setContributionCategory(contributionCategoryRepository.getById(7));  // Setting contribution category as teamBuilding
        teamBuildingDetails.setTotalPoints(teamBuildingDetails.getCount() * teamBuildingDetails.getContributionCategory().getPoints());     // setting total points (count*points)
        teamBuildingDetails.setEmpId(employeeRepositoryImp.getByEmployeeId(teamBuildingDetails.getEmpId().getId()));
        teamBuildingDetails.setActive(Active.ACTIVE); // Setting state as ACTIVE

        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(teamBuildingDetails.getContributionCategory().getName());
        notification.setEmpId(teamBuildingDetails.getEmpId().getId());
        teamBuildingDetails.setNotification(notification);

        // Saving into the database
        contributionsRepository.save(teamBuildingDetails);
        return teamBuildingDetails.getId();
    }

    public Integer saveTeamBuildingAsDraft(Contributions teamBuildingDetails) throws IOException {
        log.debug("Inside addTeamBuilding (Repository) with parameters {}", teamBuildingDetails);
        String name = employeeRepositoryImp.getEmployeeName(teamBuildingDetails.getEmpId().getId()); // Getting name
        teamBuildingDetails.setCount(1);// Setting count as 1 default
        Timestamp time = Timestamp.from(Instant.now());     // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // Creating date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian time
        String date = dateFormat.format(time);
        teamBuildingDetails.setCreatedOn(date);  // Setting CreatedOn;
        teamBuildingDetails.setCreatedBy(name);     // Setting cratedBy
        teamBuildingDetails.setLastModifiedOn(date); // Setting LastModifiedOn
        teamBuildingDetails.setLastModifiedBy(name);    // Setting LastModifiedBy
        teamBuildingDetails.setStatus(Status.DRAFT.getName());     // Setting status as Drafted
        teamBuildingDetails.setContributionCategory(contributionCategoryRepository.getById(7));  // Setting contribution category as teamBuilding
        teamBuildingDetails.setTotalPoints(teamBuildingDetails.getCount() * teamBuildingDetails.getContributionCategory().getPoints());     // setting total points (count*points)
        teamBuildingDetails.setEmpId(employeeRepositoryImp.getByEmployeeId(teamBuildingDetails.getEmpId().getId()));
        teamBuildingDetails.setActive(Active.ACTIVE); // Setting state as ACTIVE

        // Setting up notification
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(teamBuildingDetails.getContributionCategory().getName());
        notification.setEmpId(teamBuildingDetails.getEmpId().getId());
        teamBuildingDetails.setNotification(notification);

        // Saving into the database
        contributionsRepository.save(teamBuildingDetails);
        return teamBuildingDetails.getId();
    }


    public ResponseMessage getAllTeamBuilding(String id, int pageNo) throws IOException {
        log.debug("Inside getAllTeamBuilding (Repository) with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5); // Setting page size as 5
        Page<Contributions> teamBuilding = contributionsRepository.findAllByEmployeeId(id, 7, pageable); // finding data for specific page
        long totalRows = teamBuilding.getTotalElements();
        if (teamBuilding.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No TeamBuilding Activity found", arr, 0); // handling null case
        }
        List<Contributions> teamBuildingList = teamBuilding.stream().map(project -> {
            // Reducing payload
            project.setLastModifiedBy(null);
            project.setCreatedBy(null);
            project.setLastModifiedOn(null);
            project.setLastModifiedBy(null);
            project.setEmpId(null);
            return project;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, teamBuildingList, totalRows);
    }

    public Contributions getSingleTeamBuilding(Integer teamBuildingId) throws IOException {
        Contributions teamBuilding;
        teamBuilding = contributionsRepository.findById(teamBuildingId).orElse(null); // Fetching data of single TeamBuilding
        // Reducing payload
        teamBuilding.setEmpId(null);
        teamBuilding.setLastModifiedBy(null);
        teamBuilding.setLastModifiedOn(null);
        if (teamBuilding == null) {
            throw new ContripointException("No such Project exists", HttpStatus.NOT_FOUND);

        }
        return teamBuilding;
    }

    public String deleteTeamBuilding(int contributionId) throws IOException {
        Contributions teamBuilding;
        teamBuilding = contributionsRepository.findById(contributionId).orElse(null);
        if (teamBuilding == null) {
            throw new ContripointException("No such Team Building Entry exists", HttpStatus.NOT_FOUND);
        }
        int id = teamBuilding.getTeamBuilding().getId();
        contributionsRepository.deleteContribution(contributionId);
        teamBuildingRepository.deleteTeamBuilding(id);
        return "Team Building deleted";
    }
}
