package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.enumeration.Active;
import com.gemini.Contripoint.enumeration.NotificationStatus;
import com.gemini.Contripoint.enumeration.Status;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
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
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Repository
public class CertificateRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(CertificateRepositoryImp.class);

    @Autowired
    private ContributionsRepository contributionsRepository;

    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    @Autowired
    private ContributionCategoryRepository contributionCategoryRepository;

    @Autowired
    private AttachmentFileRepositoryImp attachmentFileRepositoryImp;

    @Autowired
    private AttachmentFileRepository attachmentFileRepository;

    @Autowired
    private AttachmentIdRepository attachmentIdRepository;

    @Autowired
    private CertificateTechnologyRepository certificateTechnologyRepository;

    @Autowired
    private TechnologiesRepository technologiesRepository;

    public Integer addCertificate(Contributions certificate, AttachmentId attachment) throws IOException {
        log.info("Inside addCertificate (Repository) with parameters {}", certificate, attachment);

        String name = employeeRepositoryImp.getEmployeeName(certificate.getEmpId().getId());    // Getting name of the employee who uploaded the contribution
        boolean send = false;
        if (certificate.getId() != 0) {
            Contributions certificateDetails = contributionsRepository.findContributionById(certificate.getId()); // Getting certificate if already exists
            attachment = certificateDetails.getAttachmentId(); // Stetting up attachment
            send = true;    // flag to check duplicate file
            certificate.setAttachmentId(certificateDetails.getAttachmentId()); // attaching attachment to certificate
            certificate.getAttachmentId().setName(certificateDetails.getAttachmentId().getName());
        }
        int hashCode = Arrays.hashCode(attachment.getAttachmentFile().getFile());
        if (attachmentFileRepositoryImp.checkHashcode(hashCode) != null && send == false) {
            throw new ContripointException("This file already Exists", HttpStatus.CONFLICT);    // Checking for duplicate certificate
        }

        attachment.getAttachmentFile().setHashcode(hashCode); // Setting up hashcode
        certificate.setAttachmentId(attachment);        // Setting up attachment
        Timestamp time = Timestamp.from(Instant.now()); // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));   // getting indian time
        String date = dateFormat.format(time);
        certificate.setCreatedOn(date); // Setting up created on
        certificate.setLastModifiedOn(date); // Setting up last modified on
        certificate.setCreatedBy(name); // Setting up created by
        certificate.setStatus(Status.PENDING.getName()); // Setting status to Pending Approval
        certificate.setCount(1);    // Setting count to 1 (default)
        certificate.setLastModifiedBy(name); // Setting up lastModifiedBy as the name
        certificate.setActive(Active.ACTIVE);   // Setting Status to Active

        // Linking contributions_category to contribution table.
        certificate.setContributionCategory(contributionCategoryRepository.getById(1));
        certificate.setTotalPoints(certificate.getCount() * certificate.getContributionCategory().getPoints()); // setting up total points as count*perContributionPoints.

        certificate.setEmpId(employeeRepositoryImp.getByEmployeeId(certificate.getEmpId().getId()));    // Setting up EmployeeId column

        // Setting Up Notification table
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(certificate.getContributionCategory().getName());
        notification.setEmpId(certificate.getEmpId().getId());
        certificate.setNotification(notification);
        // Saving into the Database
//        contributionsRepository.deleteById();
        contributionsRepository.save(certificate);
        return certificate.getId();
    }

    public Integer saveAsDraft(Contributions certificate, AttachmentId attachment) throws IOException {

        log.debug("Inside saveAsDraft (Repository) with parameters {} {}", certificate, attachment);
        String name = employeeRepositoryImp.getEmployeeName(certificate.getEmpId().getId());    // Getting name of the employee who uploaded the contribution
        boolean send = false;
        if (certificate.getId() != 0) {
            Contributions certificateDetails = contributionsRepository.findContributionById(certificate.getId()); // Getting certificate if already exists
            attachment = certificateDetails.getAttachmentId(); // Stetting up attachment
            send = true;    // flag to check duplicate file
            certificate.setAttachmentId(certificateDetails.getAttachmentId()); // attaching attachment to certificate
            certificate.getAttachmentId().setName(certificateDetails.getAttachmentId().getName());
        }
        int hashCode = Arrays.hashCode(attachment.getAttachmentFile().getFile());
        if (attachmentFileRepositoryImp.checkHashcode(hashCode) != null && send == false) {
            throw new ContripointException("This file already Exists", HttpStatus.CONFLICT);    // Checking for duplicate certificate
        }

        attachment.getAttachmentFile().setHashcode(hashCode); // Setting up hashcode
        certificate.setAttachmentId(attachment);        // Setting up attachment
        Timestamp time = Timestamp.from(Instant.now()); // Getting time
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));   // getting indian time
        String date = dateFormat.format(time);
        certificate.setCreatedOn(date); // Setting up created on
        certificate.setLastModifiedOn(date); // Setting up last modified on
        certificate.setCreatedBy(name); // Setting up created by
        certificate.setStatus(Status.DRAFT.getName()); // Setting status to Drafted
        certificate.setCount(1);    // Setting count to 1 (default)
        certificate.setLastModifiedBy(name); // Setting up lastModifiedBy as the name
        certificate.setActive(Active.ACTIVE);   // Setting Status to Active

        // Linking contributions_category to contribution table.
        certificate.setContributionCategory(contributionCategoryRepository.getById(1));
        certificate.setTotalPoints(certificate.getCount() * certificate.getContributionCategory().getPoints()); // setting up total points as count*perContributionPoints.

        certificate.setEmpId(employeeRepositoryImp.getByEmployeeId(certificate.getEmpId().getId()));    // Setting up EmployeeId column

        // Setting Up Notification table
        Notification notification = new Notification();
        notification.setNotificationStatus(NotificationStatus.NEW);
        notification.setContributionCategory(certificate.getContributionCategory().getName());
        notification.setEmpId(certificate.getEmpId().getId());
        certificate.setNotification(notification);
        // Saving into the Database
        contributionsRepository.save(certificate);
        return certificate.getId();
    }


    public ResponseMessage getAllCertificates(String id, int pageNo) throws IOException {
        log.debug("Inside getAllCertificate (Repository) with parameters {} {}", id, pageNo);
        Pageable pageable = PageRequest.of(pageNo, 5);  // Creating a pageable and seting its size
        Page<Contributions> certificates = contributionsRepository.findAllByEmployeeId(id, 1, pageable);    // Fetching page from database.
        long totalRows = certificates.getTotalElements(); // Getting total elements
        if (certificates.isEmpty()) {
            int[] arr = new int[0];
            return new ResponseMessage("No Certificates found", arr, 0); // Sending empty array if there is no contributions
        }
        List<Contributions> certificateList = certificates.stream().map(certificate -> {
            // Reducing the payload
            certificate.setLastModifiedBy(null);
            certificate.setCreatedBy(null);
            certificate.setLastModifiedBy(null);
            certificate.setLastModifiedOn(null);
            certificate.getAttachmentId().getAttachmentFile().setFile(null);
            certificate.getAttachmentId().setId(0);
            certificate.setEmpId(null);
            return certificate;
        }).collect(Collectors.toList());
        return new ResponseMessage(null, certificateList, totalRows);
    }

    public Contributions getSingleCertificate(int certificateId) throws IOException {
        log.debug("Inside getSingleCertificate (Repository) with parameters {}", certificateId);
        Contributions certificate;
        certificate = contributionsRepository.findById(certificateId).orElse(null);     // Fetching a specific certificate
        // Reducing payload
        certificate.getAttachmentId().setId(0);
        certificate.setLastModifiedBy(null);
        certificate.setEmpId(null);
        return certificate;
    }

    public String deleteCertificate(int contributionId) throws IOException {
        log.debug("Inside deleteCertificate (Repository) with parameters {}", contributionId);
        Contributions certificate;
        certificate = contributionsRepository.findById(contributionId).orElse(null);
        if (certificate == null) {
            throw new ContripointException("Certificate Not Found", HttpStatus.NOT_FOUND);
        }
        int attachmentId = certificate.getAttachmentId().getId();
        int certificateId = certificate.getCertificate().getId();

//        int technologyId = certificate.getCertificate().getId()
        contributionsRepository.deleteContribution(contributionId);
        attachmentIdRepository.deleteAttachmentId(attachmentId);
        attachmentFileRepository.deleteAttachmentFile(attachmentId);

        List<Integer> ids = certificateTechnologyRepository.fetchTechnologyId(certificateId);
        certificateTechnologyRepository.deleteCertificateTechnology(certificateId);
        certificateTechnologyRepository.deleteCertificate(certificateId);

        for (int i = 0; i < ids.size(); i++) {
            technologiesRepository.deleteTechnology(ids.get(i));
        }

        return "Certificate Deleted";
    }
}