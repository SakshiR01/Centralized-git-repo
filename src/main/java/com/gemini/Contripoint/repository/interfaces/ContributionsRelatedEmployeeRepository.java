package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.RelatedEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ContributionsRelatedEmployeeRepository extends JpaRepository<RelatedEmployee, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select related_employee_id from contributions_related_employee where contributions_id = ?1 ")
    List<Integer> findRelatedEmployee(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete  from contributions_related_employee where contributions_id = ?1 ")
    void deleteRelatedEmployee(int id);

}
