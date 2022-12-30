package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Month;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface MonthRepository extends JpaRepository<Month, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select month from month")
    List<String> getMonth();
}
