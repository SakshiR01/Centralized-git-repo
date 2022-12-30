package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Technology;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;


public interface TechnologiesRepository extends JpaRepository<Technology, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from technology where id = ?1")
    void deleteTechnology(int technologyId);

}
