package com.gemini.Contripoint.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gemini.Contripoint.exception.ContripointException;
import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.repository.implementation.TeamBuildingRepositoryImp;
import com.gemini.Contripoint.service.interfaces.TeamBuildingService;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TeamBuildingServiceImp implements TeamBuildingService {

    public static final Logger log = LoggerFactory.getLogger(TeamBuildingServiceImp.class);

    @Autowired
    private TeamBuildingRepositoryImp teamBuildingRepositoryImp;

    @Override
    public Integer addTeamBuilding(String teamBuildingDetails) throws IOException {
        log.debug("Inside addTeamBuilding (Service) with parameters {}", teamBuildingDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions teamBuilding = objectMapper.readValue(teamBuildingDetails, Contributions.class); // Mapping details to Contribution class
            return teamBuildingRepositoryImp.addTeamBuilding(teamBuilding);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Integer saveTeamBuildingAsDraft(String teamBuildingDetails) throws IOException {
        log.debug("Inside saveTrainingSessionsAsDraft with parameters {}", teamBuildingDetails);
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Contributions teamBuilding = objectMapper.readValue(teamBuildingDetails, Contributions.class); // Mapping details to Contribution class
            return teamBuildingRepositoryImp.saveTeamBuildingAsDraft(teamBuilding);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseMessage getAllTeamBuilding(String teamBuildingDetails) throws IOException {
        log.debug("Inside getTeamBuilding (Service) with parameters {} {}", teamBuildingDetails);
        try {
            // Extracting employeeId and pageNo from data.
            JSONObject jsonObject = new JSONObject(teamBuildingDetails);
            String empId = jsonObject.getString("empId");
            int pageNo = jsonObject.getInt("pageNo");
            pageNo--;
            return teamBuildingRepositoryImp.getAllTeamBuilding(empId, pageNo);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public Contributions getSingleTeamBuilding(Integer teamBuildingId) throws IOException {
        log.debug("Inside getSingleTeamBuilding with parameters {}", teamBuildingId);
        try {
            return teamBuildingRepositoryImp.getSingleTeamBuilding(teamBuildingId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public String deleteTeamBuilding(int contributionId) throws IOException {
        log.debug("Inside getSingleTeamBuilding with parameters {}", contributionId);
        try {
            return teamBuildingRepositoryImp.deleteTeamBuilding(contributionId);
        } catch (Exception e) {
            throw new ContripointException(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
