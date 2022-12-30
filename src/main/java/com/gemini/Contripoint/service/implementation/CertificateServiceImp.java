package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.AttachmentId;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.CertificateRepositoryImp;
import com.gemini.Contripoint.service.interfaces.CertificateService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class CertificateServiceImp implements CertificateService {

    public static final Logger log = LoggerFactory.getLogger(CertificateServiceImp.class);

    @Autowired
    private CertificateRepositoryImp certificateRepositoryImp;

    public Integer addCertificate(Contributions contributions, AttachmentId attachment) throws IOException {
        log.info("Inside addCertificate (Service) with parameters {} {}", contributions, attachment);
        try {
            return certificateRepositoryImp.addCertificate(contributions, attachment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Integer saveAsDraft(Contributions contributions, AttachmentId attachment) throws IOException {
        log.debug("Inside saveAsDraftWithFile (Service) with parameters {} {}", contributions, attachment);
        try {
            return certificateRepositoryImp.saveAsDraft(contributions, attachment);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseMessage getAllCertificates(String data) throws IOException {
        log.debug("Inside getAllCertificates (Service) with parameters {} ", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            // Parsing employeeId and pageNo from JSON data
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return certificateRepositoryImp.getAllCertificates(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public Contributions getSingleCertificate(Integer id) throws IOException {
        log.debug("Inside getSingleCertificate (Service) with parameters {} ", id);
        try {
            return certificateRepositoryImp.getSingleCertificate(id);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public byte[] downloadAttachment(int id) throws IOException {
        log.debug("Inside downloadAttachment (Service) with parameter{}", id);
        try {
            Contributions contributions = certificateRepositoryImp.getSingleCertificate(id);
            return contributions.getAttachmentId().getAttachmentFile().getFile();
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String deleteCertificate(int contributionId) throws IOException {
        log.debug("Inside deleteCertificate() (Service) with parameter{}", contributionId);
        try {
            return certificateRepositoryImp.deleteCertificate(contributionId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}