package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select er.* from event e, event_enrolled ee, enrolled en, entry er where e.id = ee.event_id and  en.id = ee.enrolled_id and en.entry_id = er.id and e.id = ?1")
    Page<Entry> getAllEntriesInAnEvent(Integer eventId, Pageable pageable);

}
