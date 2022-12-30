package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.RelatedEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface RelatedEmployeeTechnologyRepository extends JpaRepository<RelatedEmployee, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select technology_id from related_employee_technology where related_employee_id = ?1 ")
    List<Integer> findTechnologyId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from related_employee_technology where related_employee_id = ?1 ")
    void deleteTechnologyId(int id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from related_employee where id = ?1 ")
    void deleteByyId(int id);
}
