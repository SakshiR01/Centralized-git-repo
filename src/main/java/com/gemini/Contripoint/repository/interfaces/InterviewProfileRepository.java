package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Interview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface InterviewProfileRepository extends JpaRepository<Interview, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = " select profile_id from interview_profile where interview_id = ?1 ")
    List<Integer> findProfileId(int interviewId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " delete from interview_profile where interview_id = ?1")
    void deleteInterviewProfile(int interviewId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " delete from interview where id = ?1")
    void deleteByyId(int interviewId);
}
