package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.NotificationMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public interface NotificationService {

    NotificationMessage getLiveNotification(String empId, List<String> notificationId, boolean bulkApproval) throws IOException;

    Integer notificationCount(String empId);

    ArrayList<NotificationMessage> getNotifications(String empId, int pageNo);

    String markAsRead(String empId);
}
