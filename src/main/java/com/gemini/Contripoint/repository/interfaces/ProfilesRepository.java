package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ProfilesRepository extends JpaRepository<Profile, Integer> {


    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " delete from profile where id = ?1")
    void deleteByyId(Integer integer);
}
