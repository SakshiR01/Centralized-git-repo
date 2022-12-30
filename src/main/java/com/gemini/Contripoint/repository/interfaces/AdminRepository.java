package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    @Transactional
    @Query(nativeQuery = true, value = "select * from admin")
    List<String> getAllAdmin();
}
