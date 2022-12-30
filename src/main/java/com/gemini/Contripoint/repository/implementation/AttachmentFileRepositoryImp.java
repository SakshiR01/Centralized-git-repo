package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.model.AttachmentFile;
import com.gemini.Contripoint.repository.interfaces.AttachmentFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class AttachmentFileRepositoryImp {

    Logger log = LoggerFactory.getLogger(AttachmentFileRepositoryImp.class);

    @Autowired
    AttachmentFileRepository attachmentFileRepository;

    public AttachmentFile checkHashcode(int hashcode) {
        log.debug("Inside checkHashcode (Repository) with parameters ", hashcode);
        return attachmentFileRepository.checkHashcode(hashcode).orElse(null);
    }
}
