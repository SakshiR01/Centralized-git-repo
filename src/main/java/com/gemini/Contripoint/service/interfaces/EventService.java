package com.gemini.Contripoint.service.interfaces;


import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.model.Event;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface EventService {
    Integer addEvent(Event eventDetails) throws IOException;

    ResponseMessage viewSingleEvent(String data);

    List<Employee> getManagers();

    List<Employee> getManagerEmployee(String rmId);

    Integer addEventAsDraft(Event event);

    ResponseMessage viewEventLeaderboard(Integer id) throws IOException;

    String deleteEvent(Integer id) throws IOException;

    ResponseMessage enroll(String data);

    ResponseMessage enrollConformation(String data);

    ResponseMessage winnerList(Integer id) throws IOException;
}
