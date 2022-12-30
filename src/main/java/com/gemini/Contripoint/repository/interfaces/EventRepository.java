package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT ev.* from event ev, enrolled en, event_enrolled ee where ev.id = ee.event_id and ee.enrolled_id = en.id and en.employee = ?1 and ev.status IN ('UPCOMING','ONGOING') order by start_date asc")
    List<Event> getAllEvents(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT ev.* from event ev, enrolled en, event_enrolled ee where ev.id = ee.event_id and ee.enrolled_id = en.id and en.employee = ?1 and ev.status = 'UPCOMING' order by start_date asc")
    Page<Event> getUpcomingEvents(Pageable pageable, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT ev.* from event ev, enrolled en, event_enrolled ee where ev.id = ee.event_id and ee.enrolled_id = en.id and en.employee = ?1 and en.is_enrolled= true and ev.status = 'CLOSED' order by end_date desc")
    Page<Event> getClosedEvents(Pageable pageable, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from event where status IN ('PENDING APPROVAL', 'DRAFTED') and admin_employee_id = ?1")
    List<Event> getDraftedAndApproval(String employeeId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from event where admin_employee_id = ?1 order by last_modified_on desc")
    Page<Event> getmyEventActivity(String employeeId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select ev.* from event ev, employee em where ev.admin_employee_id = em.id and em.rm_id = ?1 and ev.status= 'PENDING APPROVAL' order by last_modified_on desc")
    Page<Event> reviewEvent(String adminManagerId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select * from event where id=?1")
    Event findByyId(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = "select created_by, id, points, designation, pos from (select created_by, id, points, designation,DENSE_RANK() OVER (order by points desc) as pos from (select created_by, designation, sum(total_points) as points, e.id from contributions c, employee e, event ev where c.employee_id = e.id AND c.event_id = ev.id AND c.status = 'APPROVED'  AND c.active = 1 AND c.event_id = ?1  AND CAST(c.created_on as date) >= CAST(ev.start_date as date)  group by created_by, e.designation, e.id,c.event_id order by points desc) as foo) as foo2 where pos <= 30")
    List<String> getEventLeaderboard(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = "select created_by, id, pos from (select created_by, id, points, designation,DENSE_RANK() OVER (order by points desc) as pos from (select created_by, designation, sum(total_points) as points, e.id from contributions c, employee e, event ev where c.employee_id = e.id AND c.event_id = ev.id AND c.status = 'APPROVED'  AND c.active = 1 AND c.event_id = ?1  AND CAST(c.created_on as date) >= CAST(ev.start_date as date)  group by created_by, e.designation, e.id,c.event_id order by points desc) as foo) as foo2 where pos <= 30")
    List<String> getEventLeaderboardd(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = "select enrolled_id from event_enrolled where event_id= ?1")
    List<Integer> getEnrolledList(Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from event_enrolled where event_id= ?1")
    void deleteEnrolledList(Integer id);

    @Query(nativeQuery = true, value = "select * from event e where current_date >= e.end_date")
    List<Event> getCompletedEvents();

    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT employee from enrolled e,event_enrolled ee where ee.event_id = ?1 and e.id = ee.enrolled_id")
    List<String> getEmployeesInEvent(int eventId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update event set status = 'CLOSED' where id = ?1")
    void closeEvent(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT employee from enrolled e,event_enrolled ee where ee.event_id = ?1 and e.id = ee.enrolled_id ")
    List<String> getAllEmployeeByEventId(int id);

    @Transactional
    @Query(nativeQuery = true, value = "select email from employee where employee.id = ?1")
    String getEmailByEmployeeId(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from event where id = ?1")
    Event getEventById(int eventId);

    @Transactional
    @Query(nativeQuery = true, value = " select ev.* from event ev, event_enrolled ee, enrolled en, event_contribution_category ecc, contribution_category cc where ev.id = ee.event_id and ee.enrolled_id = en.id and en.is_enrolled=true and ev.status in ('UPCOMING', 'ONGOING') and en.employee = ?1 and CAST (ecc.contribution_category_id AS integer) = cc.id   and en.contribution_category = ?2 and ev.end_date >= cast(?3 as date) group by ev.id LIMIT 1 ")
    Event getParticipatedEvents(String employeeId, String contributionCategory, String createdOn);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update event set status = 'ONGOING' where id = ?1")
    void startEvent(Integer id);

    @Query(nativeQuery = true, value = "select ev.summary from event ev, event_enrolled ee, enrolled en, event_contribution_category ecc, contribution_category cc where ev.id = ee.event_id and ee.enrolled_id = en.id and ev.id = ecc.id and CAST (ecc.contribution_category_id AS integer) = cc.id and en.is_enrolled=true and employee = ?1")
    String getEventName(String employeeId);

    @Query(nativeQuery = true, value = "select * from event where status in ('UPCOMING', 'ONGOING')")
    List<Event> getApprovedAndOngoingEvents();

    @Transactional
    @Query(nativeQuery = true, value = "select is_enrolled from enrolled where employee = ?1")
    boolean checkIsEnrolled(String id);

    @Transactional
    @Query(nativeQuery = true, value = " select DISTINCT ev.summary,ev.start_date,ev.end_date,en.certificateurl from event ev,enrolled en,event_enrolled ee where ev.id = ?1 and en.employee = ?2 and ev.id = ee.event_id and ee.enrolled_id = en.id ")
    List<String> viewSingleReward(Integer eventId, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select ev.* from event ev,winner w,event_winners ew where ev.id = ew.event_id and ew.winners_id = w.id and w.emp_id = ?1 order by ev.last_modified_on desc")
    Page<Event> findAllByEmployeeId(String empId, Pageable pageable);


    @Transactional
    @Query(nativeQuery = true, value = " select pos from (select created_by, id, points, designation,DENSE_RANK() OVER (order by points desc) as pos from (select created_by, designation, sum(total_points) as points, e.id from contributions c, employee e, event ev where c.employee_id = e.id AND c.event_id = ev.id AND c.status = 'APPROVED'  AND c.active = 1 AND c.event_id = ?1 and c.employee_id = ?2 AND CAST(c.created_on as date) >= CAST(ev.start_date as date)  group by created_by, e.designation, e.id,c.event_id order by points desc) as foo) as foo2 where pos <= 30 ")
    Integer getEmployeeRank(int eventId, String empId);

    @Query(nativeQuery = true, value = "select * from event where status in ('ONGOING')")
    List<Event> getOngoingEvents();


    @Transactional
    @Query(nativeQuery = true, value = "select DISTINCT(e.*) from event e, event_enrolled ee, enrolled en where e.id = ee.event_id and ee.enrolled_id = en.id and e.status in ('UPCOMING', 'ONGOING') and en.is_enrolled = true and en.contribution_category = ?1 and en.employee = ?2 LIMIT 1")
    Optional<Event> checkAlreadyEnrolledEvents(String contributionCategory, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select created_by, id, points, designation, pos from (select created_by, id, points, designation,DENSE_RANK() OVER (order by points desc) as pos from (select created_by, designation, sum(total_points) as points, e.id from contributions c, employee e, event ev where c.employee_id = e.id AND c.event_id = ev.id AND c.status = 'APPROVED'  AND c.active = 1 AND c.event_id = ?1  AND CAST(c.created_on as date) >= CAST(ev.start_date as date) group by created_by, e.designation, e.id,c.event_id order by points desc) as foo) as foo2 where id = ?2")
    String getWinnerDetails(Integer eventId, String empId);

    @Transactional
    @Query(nativeQuery = true, value = " select en.employee from enrolled en,event ev,event_enrolled ee where ev.id = ?1 and ev.id = ee.event_id and ee.enrolled_id = en.id and en.is_enrolled = true and en.entry_id is not null ")
    List<String> findSubmittedEntries(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = " select en.employee from enrolled en,event ev,event_enrolled ee where ev.id = ?1 and ev.id = ee.event_id and ee.enrolled_id = en.id and en.entry_id is null ")
    List<String> findPendingEntries(Integer id);

    @Transactional
    @Query(nativeQuery = true, value = "select entry_id from event e, event_enrolled ee, enrolled en , entry er where e.id = ee.event_id and ee.enrolled_id = en.id  and e.event_type = 'Non Contest' and e.id = ?1 and en.employee = ?2 group by en.id ")
    Optional<Integer> findUploadedEntry(Integer id, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select distinct en.employee from event e, event_enrolled ee, enrolled en where e.id = ee.event_id and ee.enrolled_id = en.id and e.id = ?1")
    List<String> getAllParticipants(Integer eventId);
}
