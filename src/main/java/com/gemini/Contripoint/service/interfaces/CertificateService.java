package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CertificateService {

    Integer addCertificate(Contributions contributions, AttachmentId attachment) throws IOException;

    Integer saveAsDraft(Contributions contributions, AttachmentId attachment) throws IOException;

    ResponseMessage getAllCertificates(String data) throws IOException;

    Contributions getSingleCertificate(Integer id) throws IOException;

    byte[] downloadAttachment(int id) throws IOException;

    String deleteCertificate(int contributionId) throws IOException;
}