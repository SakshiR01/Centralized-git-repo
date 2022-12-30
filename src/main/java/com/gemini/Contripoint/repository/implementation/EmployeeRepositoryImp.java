package com.gemini.Contripoint.repository.implementation;


import com.gemini.Contripoint.model.Employee;
import com.gemini.Contripoint.repository.interfaces.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EmployeeRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(EmployeeRepositoryImp.class);

    @Autowired
    private EmployeeRepository employeeRepository;


    public String getEmployeeName(String employeeId) {
        log.debug("Inside getEmployeeName (Repository) with parameters {}", employeeId);
        return employeeRepository.getByEmployeeName(employeeId);
    }

    public Employee getByEmployeeId(String employeeId) {
        log.debug("Inside getByEmployeeId (Repository) with parameters {}", employeeId);
        return employeeRepository.getById(employeeId);
    }

    public Employee getEmployeeDetails(String email) {
        log.debug("Inside getEmployeeDetails (Repository) with parameters {}", email);
        return employeeRepository.getByEmail(email);
    }

    public boolean checkIsManger(String id) {
        log.debug("Inside checkIsManager (Repository) with parameters {}", id);
        return !employeeRepository.checkManager(id).isEmpty();
    }

    public Employee getProfileData(String empId) {
        log.debug("Inside getProfileData (Repository) with parameters {}", empId);
        return employeeRepository.getById(empId);
    }


    public List<Employee> getEmployeeList() {
        log.debug("Inside getEmployeeList (Repository) with no parameters");
        return employeeRepository.getEmployeeList();
    }

    public String getByEmployeeName(String name) {
        log.debug("Inside getByEmployeeName (Repository) with parameters {}", name);
        return employeeRepository.getByEmployeeName(name);
    }

    public void updateManager(String empId, String rmId) {
        log.debug("Inside UpdateManger (Repository) with parameters {} {}", empId, rmId);
        employeeRepository.updateManager(empId, rmId);
    }

    public boolean checkIsAdmin(String email) {
        log.debug("Inside UpdateManger (Repository) with parameters  {}", email);
        return employeeRepository.checkIsAdmin(email);
    }


    public List<Employee> getManagers() {
        log.debug("Inside getManagers with no parameters");
        return employeeRepository.getManagers();
    }

    public List<String> getManagerEmployee(String rmId) {
        log.debug("Inside getManagerEmployee with no parameters");
        return employeeRepository.getManagerEmployee(rmId);
    }

    public boolean checkIsAdminManager(String id) {
        log.debug("Inside getManagerEmployee with no parameters");
        return !employeeRepository.checkIsAdminManager(id).isEmpty();
    }

    public byte[] getImage(String id) {
        log.debug("Inside getImage with parameters {}", id);
        return employeeRepository.getImage(id);

    }

    public List<String> getAllEmployeeExceptCEO() {
        log.debug("Inside getAllEmployeeExceptCEO() with no parameters");
        return employeeRepository.getAllEmployeeExceptCEO();
    }

    public void setAdmin(String admin) {
        log.debug("Inside setAdmin with parameters {}", admin);
        employeeRepository.setAdmin(admin);
    }
}
