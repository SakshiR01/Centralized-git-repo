package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Winner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface WinnerRepository extends JpaRepository<Winner, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select w.* from winner w,event ev,event_winners ew where ev.id = ew.event_id and ew.winners_id = w.id and w.emp_id = ?1 order by ev.last_modified_on desc")
    Page<Winner> findAllByEmployeeIdd(String empId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select w.reward_type from event e, event_winners ew, winner w where e.id=ew.event_id and ew.winners_id=w.id  and w.emp_id = ?1 and e.id = ?2")
    String getRewardType(String empId, Integer id);
}
