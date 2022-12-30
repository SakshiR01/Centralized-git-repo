package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.ClientFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ClientFeedbackRepository extends JpaRepository<ClientFeedback, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from client_feedback where id = ?1")
    void deleteClientFeedback(int clientFeedbackId);
}
