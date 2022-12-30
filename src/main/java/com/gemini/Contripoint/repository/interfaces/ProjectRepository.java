package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from project where id = ?1")
    void deleteProject(int projectId);

}
