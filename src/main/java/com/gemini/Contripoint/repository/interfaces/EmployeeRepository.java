package com.gemini.Contripoint.repository.interfaces;

import com.gemini.Contripoint.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    @Transactional
    @Query(nativeQuery = true, value = "select * from employee where email = LOWER(?1)")
    Employee getByEmail(String email);

    Optional<Employee> findById(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from employee where rm_id = ?1 ")
    List<Employee> checkManager(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from employee order by name")
    List<Employee> getEmployeeList();

    @Transactional
    @Query(nativeQuery = true, value = "select name from employee where id = ?1")
    String getByEmployeeName(String name);

    @Transactional
    @Query(nativeQuery = true, value = "select email from employee where id = ?1 ")
    String getEmail(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select name from employee where id = ?1 ")
    String getName(String id);

    @Query(nativeQuery = true, value = "select id from employee")
    Set<String> getAllEmpId();

    @Transactional
    @Query(nativeQuery = true, value = "select email from employee")
    Set<String> getAllEmail();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update employee set id = ?2, designation = ?3, team = ?4, rm_id = ?5, name = ?6, image = ?7 where email = ?1")
    void updateEmployeeRecord(String email, String id, String designation, String team, String rmId, String name, byte[] image);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update employee set rm_id = ?2 where id = ?1")
    void updateManager(String empId, String rmId);

    @Transactional
    @Query(nativeQuery = true, value = "select is_admin from employee where email = ?1")
    boolean checkIsAdmin(String email);


    @Transactional
    @Query(nativeQuery = true, value = "select is_admin from employee where id = ?1")
    boolean checkIssAdmin(String id);


    @Query(nativeQuery = true, value = "select e.* from employee e , (select rm_id from employee group by rm_id) as q1  where q1.rm_id = e.id")
    List<Employee> getManagers();

    @Transactional
    @Query(nativeQuery = true, value = "with recursive cte (id, name, rm_id) as (select id, name, rm_id from employee where rm_id = ?1 union all select e.id, e.name,  e.rm_id from employee e inner join cte on e.rm_id = cte.id) select id, name from cte")
    List<String> getManagerEmployee(String rmId);


    @Transactional
    @Query(nativeQuery = true, value = "select * from employee e where e.rm_id= ?1 and is_admin='1'")
    List<Employee> checkIsAdminManager(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select image from employee where id = ?1")
    byte[] getImage(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select rm_id from employee where id= ?1")
    String getrmIdByEmployeeId(String id);

    @Transactional
    @Query(nativeQuery = true, value = "select * from employee where id = ?1")
    Employee getEmployeeDetails(String employee);

    @Transactional
    @Query(nativeQuery = true, value = "select id, name from employee where id != 'GSI N 001' ")
    List<String> getAllEmployeeExceptCEO();

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update employee set is_admin = true where id = ?1")
    void setAdmin(String admin);
}
