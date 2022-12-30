package com.gemini.Contripoint.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
public class ResponseMessage<T> {
    private String message;
    private T data;
    private List<ResponseMessage> employeeList;
    private T total_rows;
    private boolean isManager;
    private int totalPoints;
    private int utilizedPoints;
    private int availablePoints;
    private int position;
    private String name;
    private String designation;
    private byte[] image;
    private boolean notifications;
    private String notificationContent;
    private String token;
    private T isAdmin;
    private T isAdminManager;
    private HashMap<Integer, Integer> participants;
    private boolean globalEnrolled;
    private String empId;
    private String eventName;
    private String startDate;
    private String endDate;
    private StringBuilder url;
    private boolean winnerDeclared;
    private boolean isEntryUploaded;
    private Integer eventId;
    private String reward;
    private Integer amount;
    private AtomicInteger entryId;
    private AtomicBoolean certificatePublished;
    private AtomicBoolean alreadyEnrolled;


    public ResponseMessage(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public ResponseMessage(T data) {
        this.data = data;
    }

    public ResponseMessage(T data, List<ResponseMessage> employeeList, int totalPoints, String name, byte[] image) {
        this.data = data;
        this.employeeList = employeeList;
        this.totalPoints = totalPoints;
        this.name = name;
        this.image = image;
    }

    public ResponseMessage(String message, T data, List<ResponseMessage> employeeList, int totalPoints, String name, byte[] image, boolean winnerDeclared, AtomicBoolean alreadyEnrolled) {
        this.message = message;
        this.data = data;
        this.employeeList = employeeList;
        this.totalPoints = totalPoints;
        this.name = name;
        this.image = image;
        this.winnerDeclared = winnerDeclared;
        this.alreadyEnrolled = alreadyEnrolled;
    }

    public ResponseMessage(T data, List<ResponseMessage> employeeList, int totalPoints, String name, byte[] image, boolean winnerDeclared, boolean isEntryUploaded, AtomicBoolean certificatePublished) {
        this.data = data;
        this.employeeList = employeeList;
        this.totalPoints = totalPoints;
        this.name = name;
        this.image = image;
        this.winnerDeclared = winnerDeclared;
        this.isEntryUploaded = isEntryUploaded;
        this.certificatePublished = certificatePublished;
    }

    public ResponseMessage(T data, List<ResponseMessage> employeeList, int totalPoints, String name, byte[] image, boolean winnerDeclared, boolean isEntryUploaded, AtomicInteger entryId, AtomicBoolean certificatePublished) {
        this.data = data;
        this.employeeList = employeeList;
        this.totalPoints = totalPoints;
        this.name = name;
        this.image = image;
        this.winnerDeclared = winnerDeclared;
        this.isEntryUploaded = isEntryUploaded;
        this.entryId = entryId;
        this.certificatePublished = certificatePublished;
    }

    public ResponseMessage(String message, T data, T total_rows, HashMap<Integer, Integer> participants) {
        this.message = message;
        this.data = data;
        this.total_rows = total_rows;
        this.participants = participants;
    }

    public ResponseMessage(T data, int totalPoints) {
        this.data = data;
        this.totalPoints = totalPoints;
    }

    public ResponseMessage(String message, T data, T total_rows) {
        this.message = message;
        this.data = data;
        this.total_rows = total_rows;
    }

    public ResponseMessage(String message, T data, int points, int position) {
        this.message = message;
        this.data = data;
        this.totalPoints = points;
        this.position = position;
    }


    public ResponseMessage(T data, boolean isManager, boolean notifications, String notificationContent) {
        this.data = data;
        this.isManager = isManager;
        this.notifications = notifications;
        this.notificationContent = notificationContent;
    }

    public ResponseMessage(T data, boolean isManager, T isAdmin, T isAdminManager, boolean notifications, String notificationContent) {
        this.data = data;
        this.isManager = isManager;
        this.isAdmin = isAdmin;
        this.isAdminManager = isAdminManager;
        this.notifications = notifications;
        this.notificationContent = notificationContent;
    }

    public ResponseMessage(T data, boolean isManager, T isAdmin, T isAdminManager, int totalPoints, int utilizedPoints, int availablePoints, boolean notifications, String notificationContent, String token) {
        this.data = data;
        this.isManager = isManager;
        this.isAdmin = isAdmin;
        this.isAdminManager = isAdminManager;
        this.totalPoints = totalPoints;
        this.utilizedPoints = utilizedPoints;
        this.availablePoints = availablePoints;
        this.notifications = notifications;
        this.notificationContent = notificationContent;
        this.token = token;

    }

    public ResponseMessage(T data, boolean isManager, T isAdmin, T isAdminManager, boolean notifications, String notificationContent, String token) {
        this.data = data;
        this.isManager = isManager;
        this.isAdmin = isAdmin;
        this.isAdminManager = isAdminManager;
        this.notifications = notifications;
        this.notificationContent = notificationContent;
        this.token = token;

    }

    public ResponseMessage(String name, String designation, int points, int position, byte[] image) {
        this.totalPoints = points;
        this.position = position;
        this.name = name;
        this.designation = designation;
        this.image = image;
    }

    public ResponseMessage(String name, String designation, int totalPoints, int position, byte[] image, boolean globalEnrolled) {
        this.totalPoints = totalPoints;
        this.position = position;
        this.name = name;
        this.designation = designation;
        this.image = image;
        this.globalEnrolled = globalEnrolled;
    }

    public ResponseMessage(T data, boolean isManager, boolean notifications, String notificationContent, String token) {
        this.data = data;
        this.isManager = isManager;
        this.notifications = notifications;
        this.notificationContent = notificationContent;
        this.token = token;

    }

    public ResponseMessage(T data, boolean isManager, int totalPoints, int utilizedPoints, int availablePoints, int position) {
        this.data = data;
        this.isManager = isManager;
        this.totalPoints = totalPoints;
        this.utilizedPoints = utilizedPoints;
        this.availablePoints = availablePoints;
        this.position = position;
    }

    public ResponseMessage(List<ResponseMessage> employeeList, boolean globalEnrolled) {
        this.employeeList = employeeList;
        this.globalEnrolled = globalEnrolled;
    }

    public ResponseMessage(List<ResponseMessage> employeeList, boolean winnerDeclared, boolean globalEnrolled) {
        this.employeeList = employeeList;
        this.winnerDeclared = winnerDeclared;
        this.globalEnrolled = globalEnrolled;
    }

    public ResponseMessage(String name, String designation, int points, int position, byte[] image, String idd) {
        this.name = name;
        this.designation = designation;
        this.totalPoints = points;
        this.position = position;
        this.image = image;
        this.empId = idd;
    }

    public ResponseMessage(String eventName, String startDate, String endDate, StringBuilder url) {
        this.eventName = eventName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.url = url;
    }

    public ResponseMessage(Integer eventId, String eventName, String reward, Integer amount, Integer position) {

        this.eventId = eventId;
        this.eventName = eventName;
        this.reward = reward;
        this.amount = amount;
        this.position = position;
    }

    public T getIsAdminManager() {
        return isAdminManager;
    }

    public void setIsAdminManager(T isAdminManager) {
        this.isAdminManager = isAdminManager;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean getIsManager() {
        return isManager;
    }

    public void setIsManager(boolean isManager) {
        this.isManager = isManager;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getTotal_rows() {
        return total_rows;
    }

    public void setTotal_rows(T total_rows) {
        this.total_rows = total_rows;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public String getNotificationContent() {
        return notificationContent;
    }

    public void setNotificationContent(String notificationContent) {
        this.notificationContent = notificationContent;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public T getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(T isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getUtilizedPoints() {
        return utilizedPoints;
    }

    public void setUtilizedPoints(int utilizedPoints) {
        this.utilizedPoints = utilizedPoints;
    }

    public int getAvailablePoints() {
        return availablePoints;
    }

    public void setAvailablePoints(int availablePoints) {
        this.availablePoints = availablePoints;
    }

    public List<ResponseMessage> getEmployeeList() {
        return employeeList;
    }

    public void setEmployeeList(List<ResponseMessage> employeeList) {
        this.employeeList = employeeList;
    }

    public HashMap<Integer, Integer> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<Integer, Integer> participants) {
        this.participants = participants;
    }

    public boolean isManager() {
        return isManager;
    }

    public void setManager(boolean manager) {
        isManager = manager;
    }

    public boolean isGlobalEnrolled() {
        return globalEnrolled;
    }

    public void setGlobalEnrolled(boolean globalEnrolled) {
        this.globalEnrolled = globalEnrolled;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public StringBuilder getUrl() {
        return url;
    }

    public void setUrl(StringBuilder url) {
        this.url = url;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public boolean isWinnerDeclared() {
        return winnerDeclared;
    }

    public void setWinnerDeclared(boolean winnerDeclared) {
        this.winnerDeclared = winnerDeclared;
    }

    public boolean isEntryUploaded() {
        return isEntryUploaded;
    }

    public void setEntryUploaded(boolean entryUploaded) {
        isEntryUploaded = entryUploaded;
    }

    public AtomicInteger getEntryId() {
        return entryId;
    }

    public void setEntryId(AtomicInteger entryId) {
        this.entryId = entryId;
    }

    public AtomicBoolean getCertificatePublished() {
        return certificatePublished;
    }

    public void setCertificatePublished(AtomicBoolean certificatePublished) {
        this.certificatePublished = certificatePublished;
    }

    public AtomicBoolean getAlreadyEnrolled() {
        return alreadyEnrolled;
    }

    public void setAlreadyEnrolled(AtomicBoolean alreadyEnrolled) {
        this.alreadyEnrolled = alreadyEnrolled;
    }
}

