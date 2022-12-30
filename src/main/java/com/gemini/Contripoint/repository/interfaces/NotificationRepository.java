package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {


    @Transactional
    @Query(nativeQuery = true, value = "select count(*) from notification as n , contributions as c, employee as e where n.id = c.notification_id AND c.employee_id = e.id AND n.notification_status = 1 AND e.id = ?1  ")
    Integer notificationCount(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select notification_message from (select n.notification_message, n.notification_status from notification as n , contributions as c where n.id = c.notification_id AND c.employee_id = ?1   order by last_modified_on desc) as result1 where NOT notification_status= 2")
    Page<String> getNotificationMessage(String empId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select notification_status from (select n.notification_message, n.notification_status from notification as n , contributions as c where n.id = c.notification_id AND c.employee_id = ?1  order by c.last_modified_on desc) as result1 where NOT notification_status= 2")
    Page<Integer> getNotificationread(String empId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update notification set notification_status = 0 from contributions c where notification.id = c.notification_id AND notification.notification_status = 1 AND c.employee_id = ?1 ")
    void markAsRead(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select * from notification where emp_id = ?1 AND notification_status = 1")
    List<String> checkUnreadNotifications(String empId);

    @Transactional
    @Query(nativeQuery = true, value = "select sum(total_points) as total from notification n, contributions c where n.id = c.notification_id AND notification_status = 1 AND c.employee_id = ?1 GROUP BY (c.employee_id)")
    Optional<Integer> calculatePoints(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select contribution_category from (select n.contribution_category, n.notification_status from notification as n , contributions as c where n.id = c.notification_id AND c.employee_id = ?1 order by last_modified_on desc) as result1 where NOT notification_status=2")
    Page<String> getNotificationType(String empId, Pageable pageable);

    @Transactional
    @Query(nativeQuery = true, value = "select last_modified_on from (select c.last_modified_on, n.notification_status from notification as n , contributions as c where n.id = c.notification_id AND c.employee_id = ?1  order by last_modified_on desc) as result1 where NOT notification_status = 2")
    Page<String> getLastModifiedOn(String empId, Pageable pageable);
}