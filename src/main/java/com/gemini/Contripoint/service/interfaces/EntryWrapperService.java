package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.EntryWrapper;
import org.springframework.stereotype.Service;

@Service
public interface EntryWrapperService {

    Integer addEntry(EntryWrapper entry);
}
