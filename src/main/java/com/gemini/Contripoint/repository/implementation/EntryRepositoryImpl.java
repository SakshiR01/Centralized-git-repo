package com.gemini.Contripoint.repository.implementation;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.model.*;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import com.gemini.Contripoint.repository.interfaces.EnrolledRepository;
import com.gemini.Contripoint.repository.interfaces.EntryRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EntryRepositoryImpl {


    public static final Logger log = LoggerFactory.getLogger(EntryRepositoryImpl.class);


    @Lazy
    @Autowired
    EntryRepository entryRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EnrolledRepository enrolledRepository;

    @Autowired
    S3StorageConfig s3Client;

    @Autowired
    EventRepository eventRepository;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${secretKey}")
    private String accessSecret;

    @Value("${region}")
    private String region;

    @Value("${bucket-name}")
    private String bucketName;

    public Entry addEntry(Entry entry) {
        return entryRepository.save(entry);
    }

    public List<ViewAllEntryModel> viewAllEntries(Integer eventId, Integer pageNo) {
        log.debug("Inside viewAllEntries (Repository) with parameters  {}", eventId);
        Pageable pageable = PageRequest.of(pageNo, 5);  // Creating a pageable and seting its size
        Page<Entry> entries = entryRepository.getAllEntriesInAnEvent(eventId, pageable);
        List<ViewAllEntryModel> entryList = new ArrayList<>();
        if (entries.getTotalElements() == 0) {
            ViewAllEntryModel viewAllEntryModel = new ViewAllEntryModel("no entries found", entries.getTotalElements());
            entryList.add(viewAllEntryModel);
            return entryList;
        }
        entries.forEach(entry -> {
            int entryId = entry.getId();
            String empId = enrolledRepository.getEmpIdFromEntryId(entryId);
            Employee employee = employeeRepository.getById(empId);
            String designation = employee.getDesignation();
            byte[] image = employee.getImage();
            String empName = employee.getName();
            ViewAllEntryModel viewAllEntryModel = new ViewAllEntryModel(empId, empName, designation, entry, image, entries.getTotalElements());
            entryList.add(viewAllEntryModel);
        });

        return entryList;
    }

    public EntryResponseMessage findListOfEntries(Integer id) {

        log.debug("Inside viewEventLeaderboard (EventRepositoryImpl) with parameters {}", id);
        List<String> submittedNames = eventRepository.findSubmittedEntries(id);
        List<String> pendingEntries = eventRepository.findPendingEntries(id);
        List<Employee> submitted = new ArrayList<>();
        List<Employee> pending = new ArrayList<>();


        for (int i = 0; i < submittedNames.size(); i++) {
            Employee employee = employeeRepository.getEmployeeDetails(submittedNames.get(i));
            // employee.setAdmin(false);
            // employee.setEmail(null);
            // employee.setRmId(null);
            // employee.setTeam(null);

            submitted.add(employee);

        }
        for (int i = 0; i < pendingEntries.size(); i++) {
            Employee employee = employeeRepository.getEmployeeDetails(pendingEntries.get(i));
            //  employee.setAdmin(false);
            // employee.setEmail(null);
            // employee.setRmId(null);
            // employee.setTeam(null);

            pending.add(employee);

        }


        return new EntryResponseMessage(submitted, pending);
    }

    public ResponseMessage getSingleEntry(Integer entryId) {
        Entry entry = entryRepository.findById(entryId).orElse(null);
        String name = enrolledRepository.findName(entryId);

        try {
            AmazonS3 s3 = s3Client.s3Client();

            // Set the presigned URL to expire after one hour.
            java.util.Date expiration = new java.util.Date();
            long expTimeMillis = Instant.now().toEpochMilli();
            expTimeMillis += 1000 * 60 * 60;
            expiration.setTime(expTimeMillis);

            // Generate the presigned URL.
            System.out.println("Generating pre-signed URL.");
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, entry.getFileName())
                            .withMethod(HttpMethod.GET)
                            .withExpiration(expiration);
            URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);
            entry.setFileURL(new StringBuilder(String.valueOf(url)));

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
        return new ResponseMessage<>(name, entry);
    }

    public EnrolledNotEnrolledResponse getEnrolledNotEnrolled(String eventId, String empId) {
        log.debug("Inside getEnrolledNotEnrolled (Repository) with parameters {} {}", empId, eventId);
        Event event = eventRepository.getEventById(Integer.parseInt(eventId));
        if (!empId.equalsIgnoreCase(event.getAdminEmployeeId())) {
            List<String> participantList = new ArrayList<>();
            event.getEnrolled().forEach(enrolled -> {
                participantList.add(enrolled.getEmployee());
            });
            List<Employee> participants = new ArrayList<>();
            participantList.forEach(participant -> {
                Employee employee = employeeRepository.getById(participant);
                Employee participantEmployee = new Employee(employee.getId(), employee.getName(), employee.getDesignation(), employee.getImage());
                participants.add(participantEmployee);
            });
            return new EnrolledNotEnrolledResponse(participants);
        } else {
            List<String> enrolledParticipant = new ArrayList<>();
            List<String> notEnrolledParticipant = new ArrayList<>();
            event.getEnrolled().forEach(enrolled -> {
                if (enrolled.isEnrolled() == true) {
                    enrolledParticipant.add(enrolled.getEmployee());
                } else {
                    notEnrolledParticipant.add(enrolled.getEmployee());
                }
            });
            List<Employee> enrolledParticipants = new ArrayList<>();
            enrolledParticipant.forEach(enrolled -> {
                Employee employee = employeeRepository.getById(enrolled);
                Employee enrolledEmployee = new Employee(employee.getId(), employee.getName(), employee.getDesignation(), employee.getImage());
                enrolledParticipants.add(enrolledEmployee);
            });
            List<Employee> notEnrolledParticipants = new ArrayList<>();
            notEnrolledParticipant.forEach(notEnrolled -> {
                Employee employee = employeeRepository.getById(notEnrolled);
                Employee notEnrolledEmployee = new Employee(employee.getId(), employee.getName(), employee.getDesignation(), employee.getImage());
                notEnrolledParticipants.add(notEnrolledEmployee);
            });
            return new EnrolledNotEnrolledResponse(enrolledParticipants, notEnrolledParticipants);
        }
    }
}
