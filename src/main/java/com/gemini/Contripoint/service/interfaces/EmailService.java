package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.EmailResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public interface EmailService {

    EmailResponse sendMail() throws IOException;

    void sendEnrolledEmail(String empId, int eventId) throws IOException;

    void sendWinnerNonwinnerEmails(Integer eventId, List<String> winnerList) throws IOException;

    String eventInvitation(Integer id) throws IOException, ParseException;

    EmailResponse sendReminderMail() throws IOException;

    String rmApprovedEmail(Integer id) throws IOException;

    String rmDeclinedEmail(Integer id) throws IOException;

    void PendingActivityApproval(int id) throws IOException;

    void sendNonContestEndEmail(Integer eventId) throws IOException;

    EmailResponse RemainderEmailFromAdmin() throws IOException;

    void winnerListEmail(Integer eventId) throws IOException;
}
