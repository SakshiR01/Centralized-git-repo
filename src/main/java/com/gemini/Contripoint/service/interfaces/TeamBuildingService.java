package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface TeamBuildingService {
    Integer addTeamBuilding(String teamBuildingDetails) throws IOException;

    Integer saveTeamBuildingAsDraft(String teamBuildingDetails) throws IOException;

    ResponseMessage getAllTeamBuilding(String data) throws IOException;

    Contributions getSingleTeamBuilding(Integer teamBuildingId) throws IOException;

    String deleteTeamBuilding(int contributionId) throws IOException;
}
