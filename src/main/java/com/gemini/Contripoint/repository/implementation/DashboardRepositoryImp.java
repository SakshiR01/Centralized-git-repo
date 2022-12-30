package com.gemini.Contripoint.repository.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.controller.NotificationController;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.*;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import com.gemini.Contripoint.repository.interfaces.NotificationRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Repository
public class DashboardRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(DashboardRepositoryImp.class);
    @Autowired
    EventRepository eventRepository;
    @Autowired
    S3StorageConfig s3Client;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;
    @Autowired
    private ContributionsRepository contributionsRepository;
    @Autowired
    private NotificationController notificationController;
    @Autowired
    private NotificationRepository notificationRepository;
    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;

    public ResponseMessage reviewActivity(String empId, int pageNo) {
        log.debug("Inside reviewActivity (Repository) with parameters {} ", empId);
        Pageable pageable = PageRequest.of(pageNo, 5);  // Setting up page size as 5
        Page<Contributions> contributions = contributionsRepository.findContributionsForReview(empId, pageable); // Getting the contributions
        long totalRows = contributions.getTotalElements(); // Getting total elements
        if (contributions.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Activities for review ", arr, 0); // Sending empty array in case of no activity to review
        }
        List<Contributions> review = contributions.stream().map(contribution -> {
            /* Reducing payload */
            if (contribution.getAttachmentId() != null) {
                contribution.setAttachmentId(null);
            }
            return contribution;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, review, totalRows);
    }

    public String changeStatus(int id, String type, String comments) throws IOException {
        log.debug("Inside changeStatus (Repository) with parameters {} {} {}", id, type, comments);
        String rmName = employeeRepositoryImp.getEmployeeName(contributionsRepository.getById(id).getEmpId().getRmId()); // Getting Rm Name
        String empId = contributionsRepository.getById(id).getEmpId().getId(); // Getting employee name
        Contributions contributions = contributionsRepository.getById(id); // getting contribution
        Notification notification = contributions.getNotification(); // getting notification
        if (notification == null) { // if notification is null create a new notification
            Notification newNotification = new Notification();
            newNotification.setEmpId(empId);
            newNotification.setNotificationStatus(NotificationStatus.NEW);
            newNotification.setContributionCategory(contributions.getContributionCategory().getName());
            contributions.setNotification(newNotification);
        }
        notification = contributions.getNotification(); // Getting notification
        Timestamp time = Timestamp.from(Instant.now());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // getting date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian date

        String date = dateFormat.format(time);
        contributions.setLastModifiedOn(date);  // Update last modified on
        contributions.setLastModifiedBy(rmName); // Updating last modified by
        contributions.setComments(comments);

        // Updating notification
        if (type.equalsIgnoreCase("approved")) { // Handling Approved case
            contributions.setStatus(Status.APPROVED.getName());  // changing status to Approved
            contributions.setTotalPoints(contributions.getCount() * contributions.getContributionCategory().getPoints()); // Setting total points
            // Get details
            String contributionCategory = contributions.getContributionCategory().getName();
            String employeeId = contributions.getEmpId().getId();
            // Get list of events employee has participated
            Event event = eventRepository.getParticipatedEvents(employeeId, contributionCategory, contributions.getCreatedOn());
            // Calculate targetTotalPoints
            if (event == null) {
                contributions.setUtilized(0);
                contributions.setAvailable(contributions.getCount() * contributions.getContributionCategory().getPoints());
            } else {
//                int targetTotalPoint = event.getTargetPoints();
                // Check for total points
//                Integer totalPoints = Integer.parseInt(contributionsRepository.getAllContributionsByEmployeeId(empId, contributions.getContributionCategory().getId()));
//                totalPoints = totalPoints + contributions.getContributionCategory().getPoints();
//                if (totalPoints > targetTotalPoint) {
//                    contributions.setUtilized(0);
//                    contributions.setAvailable(contributions.getCount() * contributions.getContributionCategory().getPoints());
//                } else {
//                    contributions.setUtilized(contributions.getCount() * contributions.getContributionCategory().getPoints());
//                    contributions.setAvailable(0);
//                }
                contributions.setUtilized(contributions.getCount() * contributions.getContributionCategory().getPoints());
                contributions.setAvailable(0);
            }
            contributions.setEvent(event);
            // Adding notification String
            notification.setNotificationMessage(employeeRepositoryImp.getEmployeeName(notification.getEmpId()) + "! Your " + notification.getContributionCategory() + " got " + type.toLowerCase() + " by " + rmName);
            notification.setNotificationStatus(NotificationStatus.UNREAD); // Marking notification as Unread
            // Saving into contributions with updated data
            contributionsRepository.save(contributions);
        }
        if (type.equalsIgnoreCase("declined")) { // handling declined case
            contributions.setStatus(Status.DECLINED.getName()); // Changing status to declined.
            contributions.setTotalPoints(0); // Changing total points to 0
            // Adding notification String
            notification.setNotificationMessage(employeeRepositoryImp.getEmployeeName(notification.getEmpId()) + "! Your " + notification.getContributionCategory() + " got " + type.toLowerCase() + " by " + rmName);
            notification.setNotificationStatus(NotificationStatus.UNREAD); //  Changing Status to UNREAD

            contributionsRepository.save(contributions);
        }
        ArrayList<Integer> notificationIds = new ArrayList<>();
        notificationIds.add(notification.getId());  // Setting up notification Id's for the contribution Notification
        List<String> empIds = new ArrayList<String>();
        empIds.add(empId);   // Getting employeeId's of employee to send notification
        notificationController.getLiveNotification(empIds, notificationIds, false); // Sending to live notification
        return contributionsRepository.getById(id).getStatus();
    }

    public ResponseMessage bulkApprove(JSONArray arr, String type, String comments) throws IOException, JSONException {
        log.debug("Inside bulkApprove with parameters {}", arr);
        String rmName = employeeRepositoryImp.getEmployeeName(contributionsRepository.getById(arr.getInt(0)).getEmpId().getRmId()); // getting rmName
        if (arr.length() == 0) {
            throw new ContripointException("Please provide id's", HttpStatus.LENGTH_REQUIRED);  // checking if there are any contributions to approve
        }
        ArrayList<Integer> notificationIds = new ArrayList<Integer>();
        Timestamp time = Timestamp.from(Instant.now()); // Getting current time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // getting current date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting Indian time

        String date = dateFormat.format(time);
        List<String> empIds = new ArrayList<>();
        if (type.equalsIgnoreCase("approved")) { // Handing Approved case
            for (int i = 0; i < arr.length(); i++) {    // looping through all the id's in array
                Contributions contribution = contributionsRepository.getById(arr.getInt(i)); // Getting contribution with current looped id.
                Notification notification = contribution.getNotification(); // Getting notification
                if (notification == null) {     // Creating a new notification if it is null
                    Notification newNotification = new Notification();
                    newNotification.setNotificationStatus(NotificationStatus.NEW);
                    newNotification.setEmpId(contribution.getEmpId().getId());
                    newNotification.setContributionCategory(contribution.getContributionCategory().getName());
                    contribution.setNotification(newNotification);
                    contributionsRepository.save(contribution);
                }
                contribution = contributionsRepository.getById(arr.getInt(i)); // Refreshing the contribution with new notification
                notification = contribution.getNotification();
                String empId = contribution.getEmpId().getId();
                empIds.add(empId);
                String empName = contribution.getCreatedBy();
                contribution.setLastModifiedOn(date); // Changing lastModified on
                contribution.setLastModifiedBy(rmName); // Changing lastModified by
                contribution.setComments(comments);
                contribution.setStatus(Status.APPROVED.getName());  // Setting status to code.
                // Get details
                String contributionCategory = contribution.getContributionCategory().getName();
                String employeeId = contribution.getEmpId().getId();
                // Get list of events employee has participated
                Event event = eventRepository.getParticipatedEvents(employeeId, contributionCategory, contribution.getCreatedOn());
                // Calculate targetTotalPoints
                if (event == null) {
                    contribution.setUtilized(0);
                    contribution.setAvailable(contribution.getCount() * contribution.getContributionCategory().getPoints());
                } else {
                    int targetTotalPoint = event.getTargetPoints();
                    // Check for total points
//                    Integer available = Integer.parseInt(contributionsRepository.getAllAvailablePointsOfEmployeeOfACategory(empId ,contribution.getContributionCategory().getId()));
//                    Integer utilized = Integer.parseInt(contributionsRepository.getAllUtilizedPointsOfEmployeeOfACategory(empId ,contribution.getContributionCategory().getId()));
//                    int totalPoints = 0;
//                    if(available>utilized){
//                        totalPoints = available;
//                    }else{
//                        totalPoints = utilized + available;
//                    }
//                    Integer totalPoints = Integer.parseInt(contributionsRepository.getAllContributionsByEmployeeId(empId, contribution.getContributionCategory().getId()));
//                    totalPoints = totalPoints + contribution.getContributionCategory().getPoints();
//                    if (totalPoints > targetTotalPoint) {
//                        contribution.setUtilized(0);
//                        contribution.setAvailable(contribution.getCount() * contribution.getContributionCategory().getPoints());
//                    } else {
//                        contribution.setUtilized(contribution.getCount() * contribution.getContributionCategory().getPoints());
//                        contribution.setAvailable(0);
//                    }
                    contribution.setUtilized(contribution.getCount() * contribution.getContributionCategory().getPoints());
                    contribution.setAvailable(0);
                    contribution.setEvent(event);
                }
                notificationIds.add(notification.getId());
                // changing notification message
                contribution.getNotification().setNotificationMessage(empName + "! Your " + notification.getContributionCategory() + " got " + type.toLowerCase() + " by " + rmName);
                // changing notification status to unread.
                contribution.getNotification().setNotificationStatus(NotificationStatus.UNREAD);
                // Saving the updated data in the database.
                contributionsRepository.save(contribution);

            }
            // Sending live notifications to the respective users
            notificationController.getLiveNotification(empIds, notificationIds, true);
        }
        if (type.equalsIgnoreCase("declined")) { // Handing the declined case.
            for (int i = 0; i < arr.length(); i++) { // Looping through the array
                Contributions contribution = contributionsRepository.getById(arr.getInt(i));    // fetching the required contribution
                Notification notification = contribution.getNotification(); // fetching its notificatoin
                if (notification == null) { // Creating anew Notification if it is null
                    Notification newNotification = new Notification();
                    newNotification.setNotificationStatus(NotificationStatus.NEW);
                    newNotification.setEmpId(contribution.getEmpId().getId());
                    newNotification.setContributionCategory(contribution.getContributionCategory().getName());
                    contribution.setNotification(newNotification);
                    contributionsRepository.save(contribution);
                }
                contribution = contributionsRepository.getById(arr.getInt(i));  // Refreshing the contribution
                notification = contribution.getNotification();
                String empId = contribution.getEmpId().getId();
                empIds.add(empId);
                String empName = contribution.getCreatedBy();   // Getting the employee Name
                contribution.setLastModifiedOn(date);   // Updating lastMofiedOn
                contribution.setLastModifiedBy(rmName);     // Updating lastMofiedby
                contribution.setComments(comments);     // Setting up comments
                contribution.setStatus(Status.DECLINED.getName());      // changing status to Approved
                contribution.setTotalPoints(0);         // Setting up total points ot Zero for declined case.
                notificationIds.add(notification.getId());  // adding notifications id to array
                // Changing the notification String.
                contribution.getNotification().setNotificationMessage(empName + "! Your " + notification.getContributionCategory() + " got " + type.toLowerCase() + " by " + rmName);
                // Changing the status of notification as UNREAD
                contribution.getNotification().setNotificationStatus(NotificationStatus.UNREAD);
                // Saving the updated data in the database
                contributionsRepository.save(contribution);
            }
            // Sending Live notification to respective users.
            notificationController.getLiveNotification(empIds, notificationIds, true);
        }
        return new ResponseMessage("Done", null);
    }

    public ResponseMessage myActivity(String empId, int pageNo) {
        log.debug("Inside myActivity (Repository) with parameters {} {}", empId, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);   // Setting up new Page size as 5
        Page<Contributions> myActivity = contributionsRepository.findMyActivity(empId, pageable);       // fetching page data on respective page.
        long totalRows = myActivity.getTotalElements();
        if (myActivity.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Recent Activities", arr, 0); // Sending empty array in case of no recent activities
        }
        List<Contributions> activities = myActivity.stream().map(activity -> {
            /* Reducing payload */
            if (activity.getAttachmentId() != null) {
                activity.getAttachmentId().setAttachmentFile(null);
            }
            return activity;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, activities, totalRows);
    }

    public ResponseMessage recentContributions(int pageNo) {
        log.debug("Inside recentContributions (Repository) with parameters", pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);  // setting up page size to 5
        Page<Contributions> contributions = contributionsRepository.getRecentContributions(pageable);     // Fetching the data.
        long totalRows = contributions.getTotalElements();
        if (contributions.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Recent Contributions", arr, 0);  // Sending empty data in case of no recent contributions
        }

        List<Contributions> contributionsList = contributions.stream().map(contribution -> {
            if (contribution.getAttachmentId() != null) {
                // Reducing payload
                contribution.setAttachmentId(null);
            }
            return contribution;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, contributionsList, totalRows);
    }

    public List<ResponseMessage> leaderboard() {
        log.debug("Inside dashboardData (Repository) with parameters {}");
        List<String> ranking = contributionsRepository.getRanking();    // Fetching the data from database
        List<ResponseMessage> rankingResponse = ranking.stream().map(rank -> {
            String[] arr = rank.split(","); // Splitting to name and id's
            String name = arr[0];
            String id = arr[1];
            int points = Integer.parseInt(arr[2]);
            String designation = arr[3];    // Getting designation
            int position = Integer.parseInt(arr[4]);    // getting position
            byte[] image = employeeRepositoryImp.getProfileData(id).getImage();  // getting image
            return new ResponseMessage(name, designation, points, position, image);     // returning
        }).collect(Collectors.toList());
        return rankingResponse;
    }


    public ResponseMessage myProfile(String empId) {
        log.debug("Inside myProfile (Repository) with parameters {}", empId);
        Employee employee = employeeRepositoryImp.getProfileData(empId);    //  Getting employee Details
        String empRank = contributionsRepository.getEmpRank(empId);         // Getting employee Rank
        boolean isManager = employeeRepositoryImp.checkIsManger(empId);     // checking if it is a manager or not
        if (empRank == null) {
            return new ResponseMessage(employee, false, 0, 0, 0, 0);
        }
        String[] arr = empRank.split(",");
        int position = Integer.parseInt(arr[0]);    // Getting its position
        String pointsData = contributionsRepository.getPoints(empId);
        String[] pointsArr = pointsData.split(",");
        int totalPoints = Integer.parseInt(pointsArr[0]);
        int utilizedPoints = Integer.parseInt(pointsArr[2]);
        int availablePoints = Integer.parseInt(pointsArr[1]);
        return new ResponseMessage(employee, isManager, totalPoints, utilizedPoints, availablePoints, position);
    }

    public Count contributionCount(String empId) throws IOException {
        log.debug("Inside contributionCount (Repository) with parameters {}", empId);
        // Setting default count as 0 for all modules
        int certificateCount = 0;
        int clientfeedbackCount = 0;
        int interviewCount = 0;
        int trainingSessionCount = 0;
        int mentorshipCount = 0;
        int projectCount = 0;
        int teamBuilding = 0;
        int selfDevelopment = 0;
        List<Integer> id = contributionsRepository.findCategory(empId); // getting only id
        List<Integer> count = contributionsRepository.contributionCount(empId); // getting only count

        if (id.size() == 0 && count.size() == 0) {
            return new Count(certificateCount, clientfeedbackCount, interviewCount, trainingSessionCount, mentorshipCount, projectCount, teamBuilding, selfDevelopment);
        } else {
            // Matching the count and id's
            for (int i = 0; i < id.size(); i++) {
                if (id.get(i) == 1) {
                    certificateCount = count.get(i);
                } else if (id.get(i) == 2) {
                    clientfeedbackCount = count.get(i);
                } else if (id.get(i) == 3) {
                    interviewCount = count.get(i);
                } else if (id.get(i) == 4) {
                    trainingSessionCount = count.get(i);
                } else if (id.get(i) == 5) {
                    mentorshipCount = count.get(i);
                } else if (id.get(i) == 6) {
                    projectCount = count.get(i);
                } else if (id.get(i) == 7) {
                    teamBuilding = count.get(i);
                } else if (id.get(i) == 8) {
                    selfDevelopment = count.get(i);
                }
            }
        }
        // Returning the data
        return new Count(certificateCount, clientfeedbackCount, interviewCount, trainingSessionCount, mentorshipCount, projectCount, teamBuilding, selfDevelopment);
    }

    public List<ResponseMessage> badgeRanking() {
        log.debug("Inside dashboardData (Repository) with parameters {}");
        List<String> ranking = contributionsRepository.getBadgeRanking();    // Getting the data from database
        List<ResponseMessage> rankingResponse = ranking.stream().map(rank -> {      // Creating a Response message
            String[] arr = rank.split(",");     // Splitting the String
            String name = arr[0];    // Getting name
            String points = arr[1];     // Getting points
            int totalPoints = Integer.parseInt(points);
            String overallRank = arr[2];     // Getting rank
            int overallRanking = Integer.parseInt(overallRank);
            // returning data
            return new ResponseMessage(name, totalPoints, overallRanking);
        }).collect(Collectors.toList());
        return rankingResponse;
    }

    public ResponseMessage viewEventList(String empId) {
        log.debug("Inside viewEventList (Repository) with parameters {}", empId);
        List<Event> events = eventRepository.getAllEvents(empId);   // Getting the data from database

        if (events.isEmpty()) {
            List<Event> list = new ArrayList<>();
            return new ResponseMessage("No events found", list);
        }
        List<Event> eventList = new ArrayList<>();
        eventList = events.stream().map(event -> {
            event.getStartDate().setHours(5);
            event.getStartDate().setMinutes(30);

            try {
                AmazonS3 s3 = s3Client.s3Client();

                // Set the presigned URL to expire after one hour.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = Instant.now().toEpochMilli();
                expTimeMillis += 1000 * 60 * 60;
                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, event.getCarousalFileName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                event.setCarousalURL(new StringBuilder(String.valueOf(url)));

                //  System.out.println("Pre-Signed URL: " + url.toString());
            } catch (AmazonServiceException e) {
                // The call was transmitted successfully, but Amazon S3 couldn't process
                // it, so it returned an error response.
                e.printStackTrace();
            } catch (SdkClientException e) {
                // Amazon S3 couldn't be contacted for a response, or the client
                // couldn't parse the response from Amazon S3.
                e.printStackTrace();
            }

            // Reducing the payload
            event.setAdminEmployeeId(null);
            event.setEnrolled(null);
            event.setTargetPoints(0);
            event.setRewards(null);
            return event;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, eventList);
    }
}