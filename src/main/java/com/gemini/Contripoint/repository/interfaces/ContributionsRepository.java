package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Contributions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ContributionsRepository extends JpaRepository<Contributions, Integer> {

    @Transactional
    @Query("from Contributions where id = ?1 AND active = 1")
    Contributions findContributionById(int id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from Contributions where employee_id = ?1 AND contribution_category_id = ?2 AND active = 1 order by last_modified_on desc")
    Page<Contributions> findAllByEmployeeId(String id, int category, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select * from(select  c.*, extract(quarter from  CAST(created_on AS date)) as quarter from contributions c where employee_id = ?1 AND active = 1 order by  extract(quarter from CAST(created_on AS date))) as abc where quarter = CASE when extract(month from CURRENT_DATE) IN (1,2,3) then 1 when extract(month from CURRENT_DATE) IN (4,5,6) then 2 when extract(month from CURRENT_DATE) IN (7,8,9) then 3 when extract(month from CURRENT_DATE) IN (10,11,12) then 4 end order by created_on desc ")
    Page<Contributions> findMyActivity(String id, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select c.*  from employee e, contributions c where e.rm_id = ?1 and e.id  = c.employee_id and c.status = 'PENDING APPROVAL' AND c.active = 1 order by c.last_modified_on desc ")
    Page<Contributions> findContributionsForReview(String empId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select * from(select  c.*,extract(quarter from  CAST(last_modified_on as date ))  as quarter from contributions c where status = 'APPROVED' AND active = 1 order by  extract(quarter from CAST(last_modified_on as date ))) as abc where quarter = CASE when extract(month from CURRENT_DATE) IN (1,2,3) then 1 when extract(month from CURRENT_DATE) IN (4,5,6) then 2 when extract(month from CURRENT_DATE) IN (7,8,9) then 3 when  extract(month from CURRENT_DATE) IN (10,11,12) then 4 end order by last_modified_on desc")
    Page<Contributions> getRecentContributions(Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select created_by, id, points, designation, pos from (select created_by, id, points, designation,DENSE_RANK() OVER (order by points desc) as pos from (select created_by, designation, sum(total_points) as points, e.id from contributions c, employee e where c.employee_id = e.id AND c.status = 'APPROVED' AND c.active = 1 group by created_by, e.designation, e.id order by points desc) as foo) as foo2 where pos <= 30")
    List<String> getRanking();

    @Transactional
    @Query(nativeQuery = true, value = "select pos, points from (select points, employee_id, DENSE_RANK() OVER (order by points desc) as pos from (select sum(total_points) as points, employee_id from contributions where  status = 'APPROVED' and active =1 group by employee_id) as foo2) as foo3 where employee_id = ?1")
    String getEmpRank(String empId);


    @Transactional
    @Query(nativeQuery = true, value = "select contribution_category_id,sum(count), case when contribution_category_id='1' then 'certification' when contribution_category_id='2' then 'ClientFeedback' when contribution_category_id='3' then 'interview' when contribution_category_id = '4' then 'trainingSession' when contribution_category_id = '5' then 'mentorship' when contribution_category_id = '6' then 'projects' when contribution_category_id = '7' then 'teamBuilding' when contribution_category_id = '8' then 'selfDevelopment' end as Category from contributions where employee_id = ?1 AND active = 1 group by contribution_category_id ")
    List<Integer> findCategory(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select sum(count), case when contribution_category_id='1' then 'certification' when contribution_category_id='2' then 'ClientFeedback' when contribution_category_id='3' then 'interview' when contribution_category_id = '4' then 'trainingSession' when contribution_category_id = '5' then 'mentorship' when contribution_category_id = '6' then 'project' when contribution_category_id = '7' then 'teamBuilding' when contribution_category_id = '8' then 'selfDevelopment' end as Category from contributions where employee_id = ?1 AND active = 1 group by contribution_category_id ")
    List<Integer> contributionCount(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select notification_message from notification where notification_status = 1 AND id = ?2 AND emp_id = ?1 ")
    ArrayList<String> getLiveNotification(String empId, int notificationId);


    @Transactional
    @Query(nativeQuery = true, value = "select c.id,c.created_by,c.contribution_category_id from contributions c where active = 1")
    ArrayList<Contributions> getContribution();

    @Transactional
    @Query(nativeQuery = true, value = "select contribution_category_id,count(*),created_by from contribution where created_by = ?1 and active = 1 group by created_by,contribution_category_id  ")
    List<Integer> getContributionType(String name);

    @Transactional
    @Query(nativeQuery = true, value = "select count(*),contribution_category_id,created_by from contribution where created_by = ?1 and active = 1 group by created_by,contribution_category_id  ")
    List<Integer> getContributionCount(String name);

    @Transactional
    @Query(nativeQuery = true, value = " select distinct rm_id from (select  rm_id,contribution_category_id,count(*) from ( select * from contributions c,employee e where c.employee_id=e.id and status ='PENDING APPROVAL' and active = 1) as q1 group by contribution_category_id , rm_id) as q ")
    List<String> getManagerId();

    @Transactional
    @Query(nativeQuery = true, value = "select  count(*) from ( select * from contributions c,employee e where c.employee_id=e.id and status ='PENDING APPROVAL' and active = 1) as q1 where rm_id= ?1 ")
    Integer getManagerPendingCount(String id);

    @Transactional
    @Query(nativeQuery = true, value = " select contribution_category_id,count(*) from ( select * from contributions c,employee e where c.employee_id=e.id and status ='PENDING APPROVAL' and active = 1) as q1 where rm_id= ?1  group by contribution_category_id , rm_id ")
    List<String> getSpecificContributionCount(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from (select  created_by,sum(total_points), DENSE_RANK() OVER(order by sum(total_points) desc) rank from contributions where status='APPROVED' AND active = 1 group by created_by order by rank asc) as foo where rank <=30 ")
    List<String> getBadgeRanking();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "delete from contributions where id = ?1")
    void deleteContribution(int contributionId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set active = 0 where status = 'APPROVED' ")
    void archive();

    @Query(nativeQuery = true, value = "select sum(total_points) as total_points, sum(available) as available_points, sum(utilized) as utilized_points from contributions where status = 'APPROVED' and employee_id = ?1 and active = 1 group by employee_id")
    String getPoints(String id);

    @Transactional
    //the above query wasn't working for me when I wanted to get the total points of an employee for sending email
    @Query(nativeQuery = true, value = "select sum(total_points) from contributions where status = 'APPROVED' and employee_id = ?1 and active = 1 group by employee_id")
    int getTotalPoints(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from contributions where contribution_category_id = ?1 and employee_id = ?2 and available != 0 and event_id is null and active = 1")
    List<Contributions> findContributionByContributionCategory(Integer e, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from contributions c where contribution_category_id = ?1 and employee_id = ?2 and available != 0 and event_id is null and active = 1 order by created_on desc")
    List<Contributions> getContributionsLatest(Integer s, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select sum(total_points) from contributions where employee_id = ?1 and contribution_category_id =?2 and status = 'APPROVED' and active = 1")
    String getAllContributionsByEmployeeId(String empId, int contributionCategory);

    @Transactional
    @Query(nativeQuery = true, value = "select cast(sum(total_points) as varchar) from contributions where employee_id = ?1 and contribution_category_id = cast(?2 as integer) and status = 'APPROVED' and active=1 and (event_id = ?3 OR event_id is null ) group by employee_id")
    Optional<String> getPointsOfCategory(String empId, String eventContributionCategory, Integer id);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set event_id = ?3 where id in ((select id from contributions where event_id = ?2 and employee_id = ?1))")
    void updateNewEnrollement(String empId, Integer oldEventId, int eventId);

    @Transactional
    @Query(nativeQuery = true, value = "select id from contributions where event_id = ?1 and employee_id = ?2")
    List<Integer> getContributionIdMappedToEvent(int id, String empId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set event_id = null where id = ?1")
    void updateEventIdNull(Integer contribution);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set available = utilized where id = ?1")
    void shiftPointsToAvailableFromUtilized(Integer contribution);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set utilized = 0 where id = ?1")
    void setUtilizedPointsToZero(Integer contribution);

    @Transactional
    @Query(nativeQuery = true, value = "select name from contribution_category where id = (select contribution_category_id from contributions where id = ?1)")
    String getContributionCategoryType(Integer contribuitonsId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set event_id = ?1 where id = ?2")
    void setEventId(Integer eventId, Integer integer);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set utilized = available where id = ?1")
    void shiftPointsToUtilizedFromAvailable(Integer contribution);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update contributions set available = 0 where id = ?1")
    void setAvailablePointsToZero(Integer contribution);

    @Transactional
    @Query(nativeQuery = true, value = "select id from contributions where contribution_category_id = (select id from contribution_category where name = ?1) and employee_id = ?2 and event_id is null")
    List<Integer> checkContribuitonOfEventIdNull(String s, String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select event_id from contributions where id = ?1")
    Integer findEventId(Integer integer);
}
