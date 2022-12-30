package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

@Service
public interface EventDashboardService {
    ResponseMessage viewUpcomingEvent(String data);

    ResponseMessage viewClosedEvent(String data);

    ResponseMessage myEventActivity(String data);

    ResponseMessage reviewEvent(String data);

    String changeStatus(String data);

    String startEndEvent();
}
