package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Enrolled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EnrolledRepository extends JpaRepository<Enrolled, Integer> {

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update enrolled set is_enrolled = true where id in (select en.id from event ev, event_enrolled ee, enrolled en where ev.id = ee.event_id and en.id = ee.enrolled_id and employee=?2 and ev.id =?1 )")
    void updateStatus(int eventId, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select is_enrolled from enrolled where id in (select en.id from event ev, event_enrolled ee, enrolled en where ev.id = ee.event_id and en.id = ee.enrolled_id and employee=?2 and ev.id =?1 )")
    boolean checkEnrolled(int eventId, String empId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = " update enrolled set certificate_name = ?1 from event ,event_enrolled  where event.id = ?2 and event_enrolled.event_id = event.id and event_enrolled.enrolled_id = enrolled.id and enrolled.employee = ?3 ")
    void updateCertificateName(String certificateFileName, int eventId, String empId);

    @Transactional
    @Query(nativeQuery = true, value = " select DISTINCT en.certificate_name from enrolled en ,event ev,event_enrolled ee where ev.id = ?1 and ee.event_id = ev.id and ee.enrolled_id = en.id and en.employee = ?2 ")
    String getCertificateName(Integer eventId, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select employee from enrolled where entry_id = ?1")
    String getEmpIdFromEntryId(int entryId);

    @Transactional
    @Query(nativeQuery = true, value = " select employee from enrolled where id in (select en.id from event ev, event_enrolled ee, enrolled en where ev.id = ee.event_id and en.id = ee.enrolled_id  and ev.id =?1 and en.is_enrolled=true and en.entry_id IS NOT NULL)")
    List<String> getEnrolledwithEntries(int eventId);

    @Transactional
    @Query(nativeQuery = true, value = " select name from employee where id = (select employee from enrolled where entry_id = ?1 LIMIT 1) ")
    String findName(Integer entryId);
}
