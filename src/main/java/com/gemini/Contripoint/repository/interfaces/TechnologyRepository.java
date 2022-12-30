package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Technologies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface TechnologyRepository extends JpaRepository<Technologies, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select Technology from technologies order by Technology")
    List<String> getTechnologyList();
}
