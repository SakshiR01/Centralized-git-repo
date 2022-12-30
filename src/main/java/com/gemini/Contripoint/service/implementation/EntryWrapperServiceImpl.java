package com.gemini.Contripoint.service.implementation;


import com.amazonaws.services.s3.AmazonS3;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.config.S3StorageConfig;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Entry;
import com.gemini.Contripoint.model.EntryWrapper;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.repository.interfaces.EntryRepository;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import com.gemini.Contripoint.service.interfaces.EntryService;
import com.gemini.Contripoint.service.interfaces.EntryWrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

@Service
public class EntryWrapperServiceImpl implements EntryWrapperService {

    @Autowired
    EntryService entryService;

    @Autowired
    EntryRepository entryRepository;

    @Autowired
    S3StorageConfig s3Client;
    @Autowired
    EventRepository eventRepository;
    @Value("${bucket-name}")
    private String bucketName;

    @Override
    public Integer addEntry(EntryWrapper entryWrapper) {
        try {
            File theDir = new File("/home/" + "contripoint_temp_files");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            ObjectMapper objectMapper = new ObjectMapper();
            String entryDetails = entryWrapper.getEntryDetails();
            Entry entry = objectMapper.readValue(entryDetails, Entry.class);

            Date date = new Date();
            entry.setLastModifiedOn(date);
            entry.setCreatedOn(date);

            if (entryWrapper.getEntryFile() != null) {


                String fileName = entryWrapper.getEntryFile().getOriginalFilename();

                String newPath = "/home/" + "contripoint_temp_files/" + fileName + ".jpg";

                File newFile = new File(newPath);

                try (FileOutputStream fos = new FileOutputStream(newFile)) {
                    fos.write(entryWrapper.getEntryFile().getBytes());
                } catch (Exception e) {
                    throw new ContripointException("Error in creating file", HttpStatus.INTERNAL_SERVER_ERROR);
                }

                String S3fileName = System.currentTimeMillis() + fileName;
                AmazonS3 s3 = s3Client.s3Client();
                s3.putObject(bucketName, S3fileName, new File(newPath));

                entry.setFileName(S3fileName);

                Entry oldEntry = entryRepository.findById(entry.getId()).orElse(null);
                if (oldEntry != null) {
                    oldEntry = entry;
                    return entryRepository.save(oldEntry).getId();
                }

                System.out.println("Finding the path to delete the file");

                Path fileToDeletePath = Paths.get(newPath);
                System.out.println("Deleting the file");
                Files.delete(fileToDeletePath);

            }

            Entry oldEntry = entryRepository.findById(entry.getId()).orElse(null);
            if (oldEntry != null) {
                if (entry.getFileName() == null) {
                    entry.setFileName(oldEntry.getFileName());
                }
                oldEntry = entry;
                return entryRepository.save(oldEntry).getId();
            }
            Event event = eventRepository.getEventById(entryWrapper.getEventId());
            event.getEnrolled().forEach(enrolled -> {
                if (enrolled.getEmployee().equalsIgnoreCase(entryWrapper.getEmpId())) {
                    enrolled.setEntry(entry);
                }
            });
            return eventRepository.save(event).getId();

        } catch (Exception e) {
            throw new ContripointException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
