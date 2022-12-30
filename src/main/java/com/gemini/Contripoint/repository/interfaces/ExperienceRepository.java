package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select level from experience order by level")
    List<String> getExperience();
}
