package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.AttachmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface AttachmentIdRepository extends JpaRepository<AttachmentId, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from attachment_id where id = ?1")
    void deleteAttachmentId(int attachmentId);
}
