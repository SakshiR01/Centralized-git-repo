package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Profiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profiles, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select profile from profiles order by profile")
    List<String> getProfile();
}
