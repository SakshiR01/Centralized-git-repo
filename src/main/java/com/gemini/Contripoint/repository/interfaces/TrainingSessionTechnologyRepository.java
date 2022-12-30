package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.TrainingAndSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TrainingSessionTechnologyRepository extends JpaRepository<TrainingAndSession, Integer> {


    @Transactional
    @Query(nativeQuery = true, value = "select technology_id from training_and_session_technology where training_and_session_id = ?1")
    List<Integer> findTechnologyId(int trainingAndSessionId);


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " delete from training_and_session_technology where training_and_session_id = ?1")
    void deleteTrainingSessionTechnology(int trainingAndSessionId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " delete from training_and_session where id = ?1")
    void deleteByyId(int trainingAndSessionId);
}
