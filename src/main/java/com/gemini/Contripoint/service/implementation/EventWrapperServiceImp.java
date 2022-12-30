package com.gemini.Contripoint.service.implementation;

import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.EventWrapper;
import com.gemini.Contripoint.service.interfaces.EventService;
import com.gemini.Contripoint.service.interfaces.EventWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class EventWrapperServiceImp implements EventWrapperService {

    @Autowired
    EventService eventService;

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

    @Override
    public Integer addEvent(EventWrapper eventDetails) {
        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String details = eventDetails.getEventDetails();
            Event event = objectMapper.readValue(details, Event.class);
            event.getStartDate().setHours(0);
            event.getStartDate().setMinutes(1);
            event.getEndDate().setHours(23);
            event.getEndDate().setMinutes(59);

            // will run when new img are to be uploaded on S3
            if (eventDetails.getBanner() != null && eventDetails.getCarousel() != null) {

                String banner = event.getSummary() + "-banner";
                String carousal = event.getSummary() + "-carousal";

                String newPath1 = "/home/" + "contripoint_temp_files/" + event.getSummary() + "-banner.jpg";

                File file1 = new File(newPath1);

                try (FileOutputStream fos = new FileOutputStream(file1)) {
                    fos.write(eventDetails.getBanner().getBytes());
                } catch (Exception e) {
                    throw new ContripointException("Error in creating file", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String newPath2 = "/home/" + "contripoint_temp_files/" + event.getSummary() + "-carousal.jpg";

                File file2 = new File(newPath2);

                try (FileOutputStream fos = new FileOutputStream(file2)) {
                    fos.write(eventDetails.getCarousel().getBytes());
                } catch (Exception e) {
                    throw new ContripointException("Error in creating file", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String fileNameBanner = System.currentTimeMillis() + "_" + banner;
                String fileNameCarousal = System.currentTimeMillis() + "_" + carousal;
                AmazonS3 s3 = s3Client.s3Client();
                s3.putObject(bucketName, fileNameBanner, new File(newPath1));
                s3.putObject(bucketName, fileNameCarousal, new File(newPath2));


                event.setBannerFileName(fileNameBanner);
                event.setCarousalFileName(fileNameCarousal);

                System.out.println("Finding the path to delete the file");

                Path fileToDeletePath = Paths.get(newPath1);
                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath);
                Path fileToDeletePath1 = Paths.get(newPath2);

                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath1);
                System.out.println("Deleted File");

            }

            return eventService.addEvent(event);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer addEventAsDraft(EventWrapper eventDetails) {
        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String details = eventDetails.getEventDetails();
            Event event = objectMapper.readValue(details, Event.class);

            event.getStartDate().setHours(0);
            event.getStartDate().setMinutes(1);
            event.getEndDate().setHours(23);
            event.getEndDate().setMinutes(59);

            // will run when new img are to be uploaded on S3
            if (eventDetails.getBanner() != null && eventDetails.getCarousel() != null) {

                String banner = event.getSummary() + "-banner";
                String carousal = event.getSummary() + "-carousal";

                String newPath1 = "/home/" + "contripoint_temp_files/" + event.getSummary() + "-banner.jpg";
                File file1 = new File(newPath1);

                try (FileOutputStream fos = new FileOutputStream(file1)) {
                    fos.write(eventDetails.getBanner().getBytes());
                } catch (Exception e) {
                    throw new ContripointException("Error in creating file", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String newPath2 = "/home/" + "contripoint_temp_files/" + event.getSummary() + "-carousal.jpg";
                File file2 = new File(newPath2);

                try (FileOutputStream fos = new FileOutputStream(file2)) {
                    fos.write(eventDetails.getCarousel().getBytes());
                } catch (Exception e) {
                    throw new ContripointException("Error in creating file", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String fileNameBanner = System.currentTimeMillis() + "_" + banner;
                String fileNameCarousal = System.currentTimeMillis() + "_" + carousal;
                AmazonS3 s3 = s3Client.s3Client();
                s3.putObject(bucketName, fileNameBanner, new File(newPath1));
                s3.putObject(bucketName, fileNameCarousal, new File(newPath2));


                event.setBannerFileName(fileNameBanner);
                event.setCarousalFileName(fileNameCarousal);

                System.out.println("Finding the path to delete the file");

                Path fileToDeletePath = Paths.get(newPath1);
                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath);
                Path fileToDeletePath1 = Paths.get(newPath2);

                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath1);
                System.out.println("Deleted File");

            }

            return eventService.addEventAsDraft(event);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
