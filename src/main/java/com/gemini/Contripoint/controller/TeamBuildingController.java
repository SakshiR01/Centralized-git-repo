package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.Contributions;
import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.TeamBuildingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TeamBuildingController {

    public static final Logger log = LoggerFactory.getLogger(TeamBuildingController.class);

    @Autowired
    TeamBuildingService teamBuildingService;

    @RequestMapping(value = "/addteambuilding", method = {RequestMethod.POST})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public HttpEntity<ResponseMessage> addTeamBuilding(@RequestBody String teamBuildingDetails) throws IOException {
        log.info("Inside addTeamBuilding with parameters {}", teamBuildingDetails);
        ResponseMessage responseMessage = new ResponseMessage(null, teamBuildingService.addTeamBuilding(teamBuildingDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }


    @RequestMapping(value = "/saveteambuildingasdraft", method = {RequestMethod.POST})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public HttpEntity<ResponseMessage> saveTeamBuildingAsDraft(@RequestBody String teamBuildingDetails) throws IOException {
        log.info("Inside addTeamBuilding with parameters {}", teamBuildingDetails);
        ResponseMessage responseMessage = new ResponseMessage(null, teamBuildingService.saveTeamBuildingAsDraft(teamBuildingDetails));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @RequestMapping(value = "/getteambuilding", method = {RequestMethod.POST})
    @CrossOrigin(origins = "*")
    @ResponseBody
    public HttpEntity<ResponseMessage> getAllTeamBuilding(@RequestBody String teamBuildingDetails) throws IOException {
        log.info("Inside addTeamBuilding with parameters {}", teamBuildingDetails);
        return new ResponseEntity(teamBuildingService.getAllTeamBuilding(teamBuildingDetails), HttpStatus.OK);
    }

    @PostMapping("/getsingleteambuilding")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<Contributions>> getSingleTeamBuilding(@RequestBody Integer teamBuildingId) throws IOException {
        log.info("Inside getSingleTeamBuilding with parameters {}", teamBuildingId);
        ResponseMessage<Contributions> responseMessage = new ResponseMessage(null, teamBuildingService.getSingleTeamBuilding(teamBuildingId));
        return new ResponseEntity(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/deleteteambuilding")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> deleteTeamBuilding(@RequestBody int contributionId) throws IOException {
        log.info("Inside deleteTeamBuilding with parameters {}", contributionId);
        ResponseMessage<String> responseMessage = new ResponseMessage<String>(null, teamBuildingService.deleteTeamBuilding(contributionId));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }
}
