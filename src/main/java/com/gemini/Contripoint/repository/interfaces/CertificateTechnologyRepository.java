package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface CertificateTechnologyRepository extends JpaRepository<Certificate, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select technology_id from certificate_technology where certificate_id = ?1")
    List<Integer> fetchTechnologyId(int certificateId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from certificate where id = ?1")
    void deleteCertificate(int certificateId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from certificate_technology where certificate_id = ?1")
    void deleteCertificateTechnology(int certificateTechnologyId);
}
