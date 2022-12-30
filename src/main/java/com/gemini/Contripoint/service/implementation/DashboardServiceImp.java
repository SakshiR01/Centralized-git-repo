package com.gemini.Contripoint.service.implementation;

import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Count;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.DashboardRepositoryImp;
import com.gemini.Contripoint.service.interfaces.DashboardService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class DashboardServiceImp implements DashboardService {

    public static final Logger log = LoggerFactory.getLogger(DashboardServiceImp.class);

    @Autowired
    private DashboardRepositoryImp dashboardRepositoryImp;

    @Override
    public ResponseMessage reviewActivity(String data) throws IOException {
        log.debug("Inside reviewActivity (Service) with parameters {} ", data);
        try {
            // Getting employeeId and pageNo from String data
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.get("empId").toString();
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return dashboardRepositoryImp.reviewActivity(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String changeStatus(String data) throws IOException {
        log.debug("Inside changeStatus (Service) with parameters {}", data);
        try {
            // Fetching status type and contribution id from JSON data
            JSONObject jsonObject = new JSONObject(data);
            String type = jsonObject.get("type").toString();
            int id = jsonObject.getInt("id");
            String comments = jsonObject.getString("comments");
            return dashboardRepositoryImp.changeStatus(id, type, comments);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage myActivity(String data) throws IOException {
        log.debug("Inside myActivity (Service) with parameters {}", data);
        try {
            // Getting employeeId and pageNo from JSON data
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.getString("empId");
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return dashboardRepositoryImp.myActivity(empId, pageNo);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage recentContributions(int pageNo) throws IOException {
        log.debug("Inside recentContributions (Service) with parameters {}", pageNo);
        try {
            pageNo--; // Decrementing pageNo for better usability
            return dashboardRepositoryImp.recentContributions(pageNo);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ResponseMessage> leaderboard() throws IOException {
        log.debug("Inside dashboardData (Service) with parametes {} ");
        try {
            return dashboardRepositoryImp.leaderboard();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseMessage bulkApprove(String data) throws IOException {
        log.debug("Inside bulkApprove (Service) with parameters {}", data);
        try {

            // Getting comments, status type and JSONArray of id's from JSON String data
            JSONObject jsonObject = new JSONObject(data);
            String comments = jsonObject.getString("comments");
            String type = jsonObject.getString("type");
            JSONArray ids = jsonObject.getJSONArray("ids");
            return dashboardRepositoryImp.bulkApprove(ids, type, comments);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage myProfile(String empId) throws IOException {
        log.debug("Inside myProfile (Service) with parameters {}", empId);
        try {
            return dashboardRepositoryImp.myProfile(empId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Count contributionCount(String empId) throws IOException {
        log.debug("Inside contributionCount (Service) with parameters {}", empId);
        try {
            return dashboardRepositoryImp.contributionCount(empId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public List<ResponseMessage> badgeRanking() throws IOException {
        log.debug("Inside badgeRanking (Service) with parametes {} ");
        try {
            return dashboardRepositoryImp.badgeRanking();

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage viewEventList(String data) throws IOException {
        log.debug("Inside viewEventList (Service) with parametes {} ", data);
        try {
            JSONObject jsonObject = new JSONObject(data);
            String empId = jsonObject.getString("empId");
            return dashboardRepositoryImp.viewEventList(empId);

        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
