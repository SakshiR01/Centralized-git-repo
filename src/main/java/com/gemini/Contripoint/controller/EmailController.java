package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.EmailResponse;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.interfaces.ContributionsRepository;
import com.gemini.Contripoint.service.interfaces.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
public class EmailController {

    public static final Logger log = LoggerFactory.getLogger(CertificateController.class);

    @Autowired
    EmailService emailService;

    @Autowired
    ContributionsRepository contributionsRepository;

    @PostMapping("/sendingEmail")
    @Scheduled(cron = "0 30 5 * * 1,5")   // Scheduling to run the function for every Monday and Friday at 11am.
    public EmailResponse sendEmail() throws IOException {
        return emailService.sendMail();
    }

    @PostMapping("/email/enrolled")
    public void sendEnrolledEmail(String empId, int eventId) throws IOException {
        log.debug("Inside sendEnrolledEmail() (Controller) with parameters , {} {}", empId, eventId);
        emailService.sendEnrolledEmail(empId, eventId);  //sending empId to emailService
    }

    @PostMapping("email/winnerNonwinner")
    public void sendWinnerNonwinnerEmails(Integer eventId, List<String> winnerList) throws IOException {
        log.debug("Inside sendEnrolledEmail() (Controller) with parameters {}, {}", eventId, winnerList);
        emailService.sendWinnerNonwinnerEmails(eventId, winnerList);

    }

    @PostMapping("/event/invitation")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ResponseMessage<String>> eventInvitation(@RequestBody Integer id) throws IOException, ParseException {
        ResponseMessage<String> responseMessage = new ResponseMessage(null, emailService.eventInvitation(id));
        return new ResponseEntity(responseMessage, HttpStatus.OK);

    }

    @PostMapping("event/reminder")
    @Scheduled(cron = "0 30 5 * * *")
    public EmailResponse sendReminderEmail() throws IOException {
        return emailService.sendReminderMail();
    }

    @PostMapping("/email/event/approved")
    public String rmApprovedEmail(@RequestBody Integer id) throws IOException {
        emailService.rmApprovedEmail(id);
        return "Email Sent";
    }

    @PostMapping("/email/event/Declined")
    public String rmDeclinedEmail(@RequestBody Integer id) throws IOException {
        emailService.rmDeclinedEmail(id);
        return "Email sent";
    }

    @PostMapping("/email/event/Pending/Activity/Approval")
    public void PendingActivityApproval(@RequestBody Integer id) throws IOException {
        emailService.PendingActivityApproval(id);
    }

    @PostMapping("/email/enroll/event")
    @Scheduled(cron = "0 30 2 * * *")
    public EmailResponse remainderEmailFromAdmin() throws IOException {
        return emailService.RemainderEmailFromAdmin();
    }
}
