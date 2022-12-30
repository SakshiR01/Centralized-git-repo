package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.ContributionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface ContributionCategoryRepository extends JpaRepository<ContributionCategory, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = " select name from contribution_category where id= ?1 ")
    String findContributionName(int id);

}
