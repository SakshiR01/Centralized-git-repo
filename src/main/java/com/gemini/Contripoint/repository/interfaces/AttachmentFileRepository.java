package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.AttachmentFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface AttachmentFileRepository extends JpaRepository<AttachmentFile, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select * from attachment_file where hashcode = ?1 AND hashcode != 0")
    Optional<AttachmentFile> checkHashcode(int hashcode);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from attachment_file where id = ?1")
    void deleteAttachmentFile(int attachmentId);
}
