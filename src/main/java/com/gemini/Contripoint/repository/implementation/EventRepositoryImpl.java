package com.gemini.Contripoint.repository.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.*;
import com.gemini.Contripoint.repository.interfaces.*;
import com.gemini.Contripoint.service.implementation.InterviewServiceImp;
import com.gemini.Contripoint.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Repository
public class EventRepositoryImpl {
    public static final Logger log = LoggerFactory.getLogger(InterviewServiceImp.class);


    @Lazy
    @Autowired
    EventRepository eventRepository;

    @Autowired
    EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    EnrolledRepository enrolledRepository;

    @Autowired
    EventContributionCategoryRepository eventContributionCategoryRepository;

    @Autowired
    ContributionsRepository contributionsRepository;

    @Autowired
    ContributionCategoryRepository contributionCategoryRepository;

    @Autowired
    S3StorageConfig s3Client;

    @Autowired
    @Lazy
    EmailService emailService;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;

    public Integer addEvent(Event event) throws IOException {
        log.debug("Inside addEvent() (RepositoryImpl) with parameters {}", event);
        try {
            List<String> empIds = new ArrayList<>();
            event.setStatus(Status.PENDING.getName());

            Timestamp time = Timestamp.from(Instant.now());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // getting date
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian date

            String date = dateFormat.format(time);  //creating date variable
            event.setCreatedOn(date);   //setting createdOn of event to today
            event.setLastModifiedOn(date);    //setting lastModifiedOn to today

            event.getEnrolled().forEach(enrolled -> {
                enrolled.setEnrolled(false);
                empIds.add(enrolled.getEmployee());
            });

            if (event.getCarousalFileName() == null && event.getBannerFileName() == null) {
                Event event1 = eventRepository.getEventById(event.getId());
                event.setCarousalFileName(event1.getCarousalFileName());
                event.setBannerFileName(event1.getBannerFileName());
            }

            eventRepository.save(event);
            emailService.PendingActivityApproval(event.getId());
            return event.getId();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public ResponseMessage viewSingleEvent(Integer id, String empId) throws IOException {
        log.debug("Inside viewSingleEvent (Repository) with parameters {}", id);

        boolean winnerDeclared = false;
        Event ee = eventRepository.getEventById(id);

        if (!ee.getWinners().isEmpty() && ee.getStatus().equalsIgnoreCase("Closed")) {
            winnerDeclared = true;
        }
        Event event = ee; // LOOK into this
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
            GeneratePresignedUrlRequest generatePresignedUrlRequest1 =
                    new GeneratePresignedUrlRequest(bucketName, event.getBannerFileName())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url1 = s3.generatePresignedUrl(generatePresignedUrlRequest1);
            event.setBannerURL(new StringBuilder(String.valueOf(url1)));

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

        String adminId = event.getAdminEmployeeId();    //fetching event admin's id
        String adminName = employeeRepositoryImp.getEmployeeName(adminId);  //fetching name of admin of event
        byte[] adminImage = employeeRepositoryImp.getImage(adminId);    //fetching image of event admin
        ArrayList<ResponseMessage> list = new ArrayList<>();    //creating a new list of type ResponseMessage
        List<String> ids = new ArrayList<>();

        if (event.getEventType().equalsIgnoreCase("Contest")) {
            for (int i = 0; i < event.getEnrolled().size(); i++) {  //iterating through all participants of event
                Employee employee = employeeRepositoryImp.getByEmployeeId(event.getEnrolled().get(i).getEmployee());    //getting an employee
                List<EventContributionCategory> c = event.getContributionCategory();
                List<String> category = new ArrayList<>();
                c.forEach(d -> {
                    category.add(d.getContributionCategoryId());
                });
//            String contributionCategory = event.getContributionCategory().get(0).getContributionCategoryId();   //getting contribution category
                String e = event.getEnrolled().get(i).getEmployee();
                int totalPoint = 0;
                for (int z = 0; z < category.size(); z++)
                    totalPoint = totalPoint + Integer.parseInt(contributionsRepository.getPointsOfCategory(e, category.get(z), id).orElse("0"));
                ResponseMessage rs = new ResponseMessage(employee, totalPoint);
                if (!ids.contains(employee.getId())) {
                    list.add(rs);
                    ids.add(employee.getId());
                }
            }
            List<EventContributionCategory> eventContributionCategoryList = event.getEventContributionCategory();
            List<String> category = new ArrayList<>();
            eventContributionCategoryList.forEach(contribution -> {
                category.add(contributionCategoryRepository.findContributionName(Integer.parseInt(contribution.getContributionCategoryId())));
            });
            AtomicBoolean alreadyEnrolled = new AtomicBoolean(false);
            Event ev = new Event();
            Event ev1 = new Event();
            List<Event> eventList = new ArrayList<>();
            List<Integer> eventIds = new ArrayList<>();
            for (String c : category) {
                ev = eventRepository.checkAlreadyEnrolledEvents(c, empId).orElse(null);
                if (ev != null) {
                    alreadyEnrolled.set(true);
                    if (!eventIds.contains(ev.getId())) {
                        eventList.add(ev);
                        eventIds.add(ev.getId());
                    }
                    ev1 = ev;
                }
            }

            List<String> y = new ArrayList<>();
            String message1 = "";
            if (ev1 != null) {
                eventList.forEach(event1 -> {
                    event1.getEventContributionCategory().forEach(x -> {
                        y.add(contributionCategoryRepository.findContributionName(Integer.parseInt(x.getContributionCategoryId())));
                    });
                });
                String message = "";
                for (String i : y) {
                    if (!message.contains(i)) {
                        message = message + i + ", ";
                    }
                }
                AtomicReference<String> events = new AtomicReference<>("");
                eventList.forEach(event1 -> {
                    events.set(events + event1.getSummary() + ", ");
                });
                if (alreadyEnrolled.get() == true) {
                    message1 = "You are already enrolled in " + events + " event having " + message + " as contribution category. If you enroll in this event, points from previous event would be shifted to this event.";
                }
            }
            List<String> z = new ArrayList<>();
            event.getEventContributionCategory().forEach(category1 -> {
                z.add(category1.getContributionCategoryId());
            });
            int employeeTotalPoints = 0;
            for (int i = 0; i < z.size(); i++) {
                employeeTotalPoints = employeeTotalPoints + Integer.parseInt(contributionsRepository.getPointsOfCategory(empId, event.getContributionCategory().get(i).getContributionCategoryId(), id).orElse("0"));
            }
            event.getStartDate().setHours(5);
            event.getStartDate().setMinutes(30);
            ResponseMessage rs = new ResponseMessage(message1, event, list, employeeTotalPoints, adminName, adminImage, winnerDeclared, alreadyEnrolled);
            return rs;
        } else {
            AtomicBoolean certificatePublished = new AtomicBoolean(false);
            if (eventRepository.findUploadedEntry(id, empId).orElse(null) != null) {
                AtomicInteger eventEntry = new AtomicInteger();
                event.getEnrolled().forEach(enrolled -> {
                    Employee employee = employeeRepositoryImp.getByEmployeeId(enrolled.getEmployee());
                    ResponseMessage rs = new ResponseMessage(employee);
                    list.add(rs);
                });
                event.getEnrolled().forEach(enrolled -> {
                    if (enrolled.getEmployee().equalsIgnoreCase(empId)) {
                        eventEntry.set(enrolled.getEntry().getId());
                    }
                });
                event.getEnrolled().forEach(enrolled -> {
                    if (enrolled.getCertificateName() != null) {
                        certificatePublished.set(true);
                    }
                });
                event.getStartDate().setHours(5);
                event.getStartDate().setMinutes(30);
                return new ResponseMessage(event, list, 0, adminName, adminImage, false, true, eventEntry, certificatePublished);
            } else {
                event.getEnrolled().forEach(enrolled -> {
                    Employee employee = employeeRepositoryImp.getByEmployeeId(enrolled.getEmployee());
                    ResponseMessage rs = new ResponseMessage(employee);
                    list.add(rs);
                });
                event.getEnrolled().forEach(enrolled -> {
                    if (enrolled.getCertificateName() != null) {
                        certificatePublished.set(true);
                    }
                });
                event.getStartDate().setHours(5);
                event.getStartDate().setMinutes(30);
                return new ResponseMessage(event, list, 0, adminName, adminImage, false, false, certificatePublished);
            }
        }
    }


    public ResponseMessage viewEventLeaderboard(Integer id) {

        log.debug("Inside viewEventLeaderboard (EventRepositoryImpl) with parameters {}", id);

        List<String> ranking = eventRepository.getEventLeaderboard(id);    // Fetching the data from database

        //Make sure that ranking does not go empty
        if (ranking.size() == 0) {
            Event event = eventRepository.getEventById(id); //getting event by id
            List<Enrolled> enrolledEmployees = event.getEnrolled(); //getting list of enrolled
            List<ResponseMessage> emptyLeaderboard = new ArrayList<>(); //creating an empty list for empty leaderboard
            List<String> distinctEmployee = new ArrayList<>();
            List<Enrolled> e = new ArrayList<>();
            enrolledEmployees.forEach(enrolled -> {
                if (enrolled.isEnrolled()) {
                    e.add(enrolled);
                }
            });
            e.forEach(enrolled -> {
                if (!distinctEmployee.contains(enrolled.getEmployee())) {
                    distinctEmployee.add(enrolled.getEmployee());
                }
            });

//                enrolledEmployees.forEach(enrolled -> {
//                    for(int i=0;i<distinctEmployee.size();i++){
//                        if(!distinctEmployee.get(i).getEmployee().equalsIgnoreCase(enrolled.getEmployee())){
//                            distinctEmployee.add(enrolled);
//                        }
//                    }
//                });

            boolean globalEnrolled = false;
            for (String employee : distinctEmployee) {  //iterating through enrolled participants
                String employeeName = employeeRepositoryImp.getEmployeeName(employee);    //getting employee name via enrolled table's employee field (which stores empId)
                byte[] image = employeeRepositoryImp.getImage(employee);   //getting employee profile picture
                Employee emp = employeeRepositoryImp.getByEmployeeId(employee);   //getting the employee
                String designation = emp.getDesignation();  //getting employee designation
                globalEnrolled = true;
                emptyLeaderboard.add(new ResponseMessage(employeeName, designation, 0, 1, image, employee));    //adding entry to emptyLeaderboard list
            }
            return new ResponseMessage<>(emptyLeaderboard, globalEnrolled);    //returning list of participants
        }

        List<ResponseMessage> rankingResponse = ranking.stream().map(rank -> {
            String[] arr = rank.split(","); // Splitting to name and id's
            String name = arr[0];
            String idd = arr[1];
            int points = Integer.parseInt(arr[2]);
            String designation = arr[3];    // Getting designation
            int position = Integer.parseInt(arr[4]);    // getting position
            byte[] image = employeeRepositoryImp.getProfileData(idd).getImage();  // getting image
            return new ResponseMessage(name, designation, points, position, image, idd);     // returning
        }).collect(Collectors.toList());

        return new ResponseMessage(rankingResponse, true);
    }


    public List<Employee> getManagers() {
        log.debug("Inside getManager (Repository) with no parameters");
        List<Employee> managers = employeeRepositoryImp.getManagers();
        managers.forEach(m -> {
            m.setDesignation(null);
            m.setEmail(null);
            m.setImage(null);
            m.setRmId(null);
            m.setTeam(null);
        });
        return managers;
    }

    public List<Employee> getManagerEmployee(String rmId) {
        log.debug("Inside getManagerEmployee (Repository) with no parameters");
        List<String> mangers = new ArrayList<>();
        if (rmId.equalsIgnoreCase("GSI N 001")) {
            mangers = employeeRepositoryImp.getAllEmployeeExceptCEO();
        } else {
            mangers = employeeRepositoryImp.getManagerEmployee(rmId);
        }
        List<String> managers = new ArrayList<>();
        mangers.forEach(m -> {
            String[] n = m.split(",");
            managers.add(n[0]);
            managers.add(n[1]);
        });
        List<Employee> employeeList = new ArrayList<>(8);
        for (int i = 0; i < mangers.size(); i++) {
            employeeList.add(new Employee());
        }
        for (int i = 0, j = 0; i < managers.size(); i = i + 2, j++) {
            employeeList.get(j).setId(managers.get(i));
        }
        for (int i = 1, j = 0; i < managers.size(); i = i + 2, j++) {
            employeeList.get(j).setName(managers.get(i));
        }
        return employeeList;
    }

    public Integer addEventAsDraft(Event event) {
        log.debug("Inside addEvent() (RepositoryImpl) with parameters {}", event);
        log.debug("Inside addEvent() (RepositoryImpl) with parameters {}", event);
        try {
            List<String> empIds = new ArrayList<>();
            event.setStatus(Status.DRAFT.getName());

            Timestamp time = Timestamp.from(Instant.now());
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // getting date
            dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian date

            String date = dateFormat.format(time);  //creating date variable
            event.setCreatedOn(date);   //setting createdOn of event to today
            event.setLastModifiedOn(date);    //setting lastModifiedOn to today

            event.getEnrolled().forEach(enrolled -> {
                enrolled.setEnrolled(false);
                empIds.add(enrolled.getEmployee());
            });

            if (event.getCarousalFileName() == null && event.getBannerFileName() == null) {
                Event event1 = eventRepository.getEventById(event.getId());
                event.setCarousalFileName(event1.getCarousalFileName());
                event.setBannerFileName(event1.getBannerFileName());
            }

            eventRepository.save(event);
            return event.getId();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    public String deleteEvent(Integer id) {
        log.debug("Inside deleteEvent (Repository) with parameters {}", id);
        Event event;
        event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ContripointException("Event Not Found", HttpStatus.NOT_FOUND);
        }
        List<Integer> enrolledList = eventRepository.getEnrolledList(id);
        eventRepository.deleteEnrolledList(id);
        for (int i = 0; i < enrolledList.size(); i++) {
            enrolledRepository.deleteById(enrolledList.get(i));
        }
        List<Integer> deleteCategoryList = eventContributionCategoryRepository.getCategoryList(id);
        eventContributionCategoryRepository.deleteCategoryList(id);
        eventRepository.deleteById(id);
        for (int i = 0; i < deleteCategoryList.size(); i++) {
            eventContributionCategoryRepository.deleteByyId(deleteCategoryList.get(i));
        }
        return "Event Deleted";
    }

    public ResponseMessage enroll(int eventId, String empId) {

        Event event = eventRepository.getEventById(eventId);
        if (event.getEventType().equalsIgnoreCase("Contest")) {

            List<EventContributionCategory> eventContributionCategoryList = event.getEventContributionCategory();
            List<String> category = new ArrayList<>();
            eventContributionCategoryList.forEach(contribution -> {
                category.add(contributionCategoryRepository.findContributionName(Integer.parseInt(contribution.getContributionCategoryId())));
            });
            AtomicBoolean alreadyEnrolled = new AtomicBoolean(false);

            Event ev = new Event();
            List<Event> eventList = new ArrayList<>();
            for (String c : category) {
                ev = eventRepository.checkAlreadyEnrolledEvents(c, empId).orElse(null);
                if (ev != null) {
                    alreadyEnrolled.set(true);
                    eventList.add(ev);
                }
            }
            List<String> y = new ArrayList<>();
            if (ev != null) {
                eventList.forEach(event1 -> {
                    event1.getEventContributionCategory().forEach(x -> {
                        y.add(contributionCategoryRepository.findContributionName(Integer.parseInt(x.getContributionCategoryId())));
                    });
                });
                String message = "";
                for (String i : y) {
                    if (!message.contains(i)) {
                        message = message + i + ", ";
                    }
                }
                AtomicReference<String> events = new AtomicReference<>("");
                eventList.forEach(event1 -> {
                    events.set(events + event1.getSummary() + ", ");
                });
                if (alreadyEnrolled.get() == true) {
                    ResponseMessage rs = new ResponseMessage("You are already enrolled in " + events + " event having " + message + " as contribution category. If you enroll in this event, points from previous event would be shifted to this event.", null);
                    return rs;
                }
            }
            enrolledRepository.updateStatus(eventId, empId);
            List<EventContributionCategory> eventContributionCategory = event.getContributionCategory();
            List<Integer> eventContributionCategoryIds = new ArrayList<>();
            eventContributionCategory.forEach(e -> {
                eventContributionCategoryIds.add(Integer.parseInt(e.getContributionCategoryId()));
            });

            for (int i = 0; i < eventContributionCategoryIds.size(); i++) {
                List<Contributions> contributions = contributionsRepository.findContributionByContributionCategory(eventContributionCategoryIds.get(i), empId);
                for (int j = 0; j < contributions.size(); j++) {
                    contributions.get(j).setUtilized(contributions.get(j).getTotalPoints());
                    contributions.get(j).setAvailable(0);
                    contributions.get(j).setEvent(event);
                    contributionsRepository.save(contributions.get(j));
                }
            }
        } else {
            event.getEnrolled().forEach(enrolled -> {
                if (enrolled.getEmployee().equalsIgnoreCase(empId)) {
                    enrolled.setEnrolled(true);
                }
            });
            eventRepository.save(event);
        }
        return new ResponseMessage("Enrolled", null);
    }

    public Event getEvent(Integer eventId) {
        return eventRepository.findByyId(eventId);
    }

    public Integer addRewards(Event event) {
        Event e = eventRepository.save(event);
        return e.getId();
    }

    public ResponseMessage enrollConformation(Integer eventId, String empId) {
        Event event = eventRepository.getEventById(eventId);
        List<EventContributionCategory> eventContributionCategoryList = event.getEventContributionCategory();
        List<String> category = new ArrayList<>();
        eventContributionCategoryList.forEach(contribution -> {
            category.add(contributionCategoryRepository.findContributionName(Integer.parseInt(contribution.getContributionCategoryId())));
        });
        List<Event> eventList = new ArrayList<>();
        List<Integer> eventIds = new ArrayList<>();
        Event ev = new Event();
        for (String c : category) {
            ev = eventRepository.checkAlreadyEnrolledEvents(c, empId).orElse(null);
            if (ev != null) {
                if (!eventIds.contains(ev.getId())) {
                    eventList.add(ev);
                    eventIds.add(ev.getId());
                }
            }
        }
        for (int i = 0; i < eventList.size(); i++) {
            eventList.get(i).getEnrolled().forEach(employee -> {
                if (employee.getEmployee().equalsIgnoreCase(empId)) {
                    employee.setEnrolled(false);
                }
            });
            eventRepository.save(eventList.get(i));     // un-enrolling and saving the events
        }
        for (int a = 0; a < eventList.size(); a++) {
            List<Integer> contributionIds = contributionsRepository.getContributionIdMappedToEvent(eventList.get(a).getId(), empId);
            contributionIds.forEach(contribution -> {
                contributionsRepository.updateEventIdNull(contribution);
                contributionsRepository.shiftPointsToAvailableFromUtilized(contribution);
                contributionsRepository.setUtilizedPointsToZero(contribution);
            });
            // Now employee is fully un-enrolled.
            enrolledRepository.updateStatus(eventId, empId); // enrolling to new event
            for (int i = 0; i < category.size(); i++) {
                for (int j = 0; j < contributionIds.size(); j++) {
                    String contributionCategory = contributionsRepository.getContributionCategoryType(contributionIds.get(j));
                    if (category.get(i).equalsIgnoreCase(contributionCategory)) {
                        Integer id = contributionsRepository.findEventId(contributionIds.get(j));
                        if (id == null) {
                            contributionsRepository.setEventId(eventId, contributionIds.get(j));
                            contributionsRepository.shiftPointsToUtilizedFromAvailable(contributionIds.get(j));
                            contributionsRepository.setAvailablePointsToZero(contributionIds.get(j));
                        }
                    } else {
                        List<Integer> contributionId = contributionsRepository.checkContribuitonOfEventIdNull(category.get(i), empId);
                        contributionId.forEach(c -> {
                            contributionsRepository.setEventId(eventId, c);
                            contributionsRepository.shiftPointsToUtilizedFromAvailable(c);
                            contributionsRepository.setAvailablePointsToZero(c);
                        });
                    }
                }
            }
        }
        return new ResponseMessage("Enrolled", null);
    }

    public ResponseMessage winnerList(Integer id) {

        boolean winnerDeclared = true;
        boolean globalEnrolled = true;
        log.debug("Inside viewEventLeaderboard (EventRepositoryImpl) with parameters {}", id);
        Event e = eventRepository.getEventById(id);
        List<Winner> winners = e.getWinners();
        List<String> ranking = new ArrayList<>();
        winners.forEach(winner -> {
            String empId = winner.getEmpId();
            String winnerDetail = eventRepository.getWinnerDetails(id, empId);
            if (winnerDetail != null) {
                ranking.add(winnerDetail);
            }
        });

        List<String> r = new ArrayList<>();
        ranking.forEach(rank -> {
            if (!r.contains(rank)) {
                r.add(rank);
            }
        });

        List<ResponseMessage> rankingResponse = r.stream().map(rank -> {
            String[] arr = rank.split(","); // Splitting to name and id's
            String name = arr[0];
            String idd = arr[1];
            int points = Integer.parseInt(arr[2]);
            String designation = arr[3];    // Getting designation
            int position = Integer.parseInt(arr[4]);    // getting position
            byte[] image = employeeRepositoryImp.getProfileData(idd).getImage();  // getting image
            return new ResponseMessage(name, designation, points, position, image, idd);     // returning
        }).collect(Collectors.toList());
        return new ResponseMessage(rankingResponse, winnerDeclared, globalEnrolled);
    }
}