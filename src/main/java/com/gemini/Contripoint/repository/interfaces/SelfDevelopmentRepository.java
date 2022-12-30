package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.SelfDevelopment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface SelfDevelopmentRepository extends JpaRepository<SelfDevelopment, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from self_development where id = ?1")
    void deleteSelfDevelopment(int selfDevelopmentId);
}
