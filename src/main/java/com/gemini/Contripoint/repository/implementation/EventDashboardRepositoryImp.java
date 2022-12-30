package com.gemini.Contripoint.repository.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Repository
public class EventDashboardRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(DashboardRepositoryImp.class);


    @Autowired
    EventRepository eventRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    S3StorageConfig s3Client;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;

    public ResponseMessage viewUpcomingEvent(int pageNo, String empId) throws IOException {

        Pageable pageable = PageRequest.of(pageNo, 4);  // Setting up page size as 8
        Page<Event> events = eventRepository.getUpcomingEvents(pageable, empId);

        events.forEach(event -> {
            try {
                AmazonS3 s3 = s3Client.s3Client();

                // Set the presigned URL to expire after one hour.
                java.util.Date expiration = new java.util.Date();
                long expTimeMillis = Instant.now().toEpochMilli();
                expTimeMillis += 1000 * 60 * 60;
                expiration.setTime(expTimeMillis);

                // Generate the presigned URL.
                GeneratePresignedUrlRequest generatePresignedUrlRequest =
                        new GeneratePresignedUrlRequest(bucketName, event.getBannerFileName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                event.setBannerURL(new StringBuilder(String.valueOf(url)));

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

        });
        long totalRows = events.getTotalElements(); // Getting total elements
        if (events.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Upcoming Events", arr, 0);
        }
        return new ResponseMessage(null, events, totalRows);
    }


    public ResponseMessage viewClosedEvent(int pageNo, String empId) {
        log.debug("Inside viewClosedEvent (Repository) with parameters {}", pageNo);
        Pageable pageable = PageRequest.of(pageNo, 4);  // Setting up page size as 8
        Page<Event> events = eventRepository.getClosedEvents(pageable, empId);
        events.forEach(event -> {
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
                        new GeneratePresignedUrlRequest(bucketName, event.getBannerFileName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                event.setBannerURL(new StringBuilder(String.valueOf(url)));

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

        });
        long totalRows = events.getTotalElements(); // Getting total elements
        if (events.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No closed Events", arr, 0);
        }
        return new ResponseMessage(null, events, totalRows);
    }

    public ResponseMessage myEventActivity(String employeeId, int pageNo) {

        boolean admin = employeeRepository.checkIssAdmin(employeeId);
        if (admin == true) {
            Pageable pageable = PageRequest.of(pageNo, 4);  // Setting up page size as 8
            Page<Event> eventList = eventRepository.getmyEventActivity(employeeId, pageable);
            eventList.forEach(event -> {
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
                            new GeneratePresignedUrlRequest(bucketName, event.getBannerFileName())
                                    .withMethod(HttpMethod.GET)
                                    .withExpiration(expiration);
                    URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                    event.setBannerURL(new StringBuilder(String.valueOf(url)));

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

            });
            long totalRows = eventList.getTotalElements(); // Getting total elements
            if (eventList.isEmpty()) {
                int[] arr = new int[0];
                return new ResponseMessage("No Events", arr, 0);
            }
            return new ResponseMessage(null, eventList, totalRows);
        } else {
            int[] arr = new int[0];
            return new ResponseMessage("No Events", arr, 0);
        }
    }

    public ResponseMessage reviewEvent(String adminManagerId, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 4);  // Setting up page size as 8
        Page<Event> eventList = eventRepository.reviewEvent(adminManagerId, pageable);
        eventList.forEach(event -> {
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
                        new GeneratePresignedUrlRequest(bucketName, event.getBannerFileName())
                                .withMethod(HttpMethod.GET)
                                .withExpiration(expiration);
                URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
                event.setBannerURL(new StringBuilder(String.valueOf(url)));

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

        });
        long totalRows = eventList.getTotalElements(); // Getting total elements
        if (eventList.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Review Events", arr, 0);
        }
        return new ResponseMessage(null, eventList, totalRows);
    }

    public String changeStatus(int id, String type) {

        Event event = eventRepository.findByyId(id);

        Timestamp timeString = Timestamp.from(Instant.now());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z"); // getting date
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Getting indian date

        String dateString = dateFormat.format(timeString);  //creating date variable

        if (type.equalsIgnoreCase("Approved")) {
            Date date = new Date();
            Timestamp time = new Timestamp(date.getTime());
            Date today = new Date(time.getTime());
            Date sd = event.getStartDate();
            Date startDate = new Date(sd.getTime());
            event.setLastModifiedOn(dateString);
            if (startDate.before(today)) {

                event.setStatus(Status.ONGOING.getName());
            }
            if (startDate.after(today)) {
                event.setStatus(Status.UPCOMING.getName());
            }

            eventRepository.save(event);
            return "Approved";
        } else {
            event.setLastModifiedOn(dateString);
            event.setStatus(Status.DECLINED.getName());
            eventRepository.save(event);
            return "Declined";
        }
    }

    public String startEndEvent() {
        // Find all upcoming Events
        List<Event> events = eventRepository.getApprovedAndOngoingEvents();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).getStatus().equalsIgnoreCase("Upcoming")) {
                Date date = new Date();
                Timestamp time = new Timestamp(date.getTime());
                Date today = new Date(time.getTime());
                String TodayDate = today.toString().substring(0, 10);
                Date sd = events.get(i).getStartDate();
                Date startDate = new Date(sd.getTime());
                String StartDate = startDate.toString().substring(0, 10);
                if (TodayDate.equalsIgnoreCase(StartDate)) {
                    events.get(i).setStatus(Status.ONGOING.getName());
                    eventRepository.save(events.get(i));
                }
            } else if (events.get(i).getStatus().equalsIgnoreCase("Ongoing")) {
                Date date = new Date();
                Timestamp time = new Timestamp(date.getTime());
                Date today = new Date(time.getTime());
                String TodayDate = today.toString().substring(0, 10);
                Date ed = events.get(i).getEndDate();
                Date endDate = new Date(ed.getTime());
                String EndDate = endDate.toString().substring(0, 10);
                if (ed.before(today)) {
                    events.get(i).setStatus(Status.CLOSED.getName());
                    eventRepository.save(events.get(i));
                }
            }
        }
        return "Started";
    }
}
