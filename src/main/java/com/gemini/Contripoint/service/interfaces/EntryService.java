package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface EntryService {
    Entry addEntry(Entry entry) throws IOException;

    List<ViewAllEntryModel> viewAllEntries(String data) throws IOException;

    EntryResponseMessage findListOfEntries(String id) throws IOException;

    ResponseMessage getSingleEntry(String entryId);

    String publishCertificates(String eventId) throws IOException;

    EnrolledNotEnrolledResponse getEnrolledNotEnrolled(String data);
}
