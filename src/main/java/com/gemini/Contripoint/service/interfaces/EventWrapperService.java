package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.EventWrapper;
import org.springframework.stereotype.Service;

@Service
public interface EventWrapperService {
    Integer addEvent(EventWrapper eventDetails);

    Integer addEventAsDraft(EventWrapper eventDetails);

}
