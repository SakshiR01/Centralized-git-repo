package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.EventContributionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface EventContributionCategoryRepository extends JpaRepository<EventContributionCategory, Integer> {


    @Transactional
    @Query(nativeQuery = true, value = "select event_contribution_category_id from event_event_contribution_category where event_id = ?1")
    List<Integer> getCategoryList(Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from event_event_contribution_category where event_id= ?1")
    void deleteCategoryList(Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from event_contribution_category where id= ?1")
    void deleteByyId(Integer id);
}
