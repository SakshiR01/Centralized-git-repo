package com.gemini.Contripoint.controller;

import com.gemini.Contripoint.model.ResponseMessage;
import com.gemini.Contripoint.service.interfaces.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class DashboardController {
    public static final Logger log = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    DashboardService dashboardService;

    @Autowired
    NotificationController notificationController;

    @PostMapping("/reviewActivity")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> reviewActivity(@RequestBody String data) throws IOException {
        log.info("Inside reviewActivity with parameters {} ", data);
        return new ResponseEntity(dashboardService.reviewActivity(data), HttpStatus.OK);
    }

    @PostMapping("/changestatus")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage<String>> changeStatus(@RequestBody String data) throws IOException {
        log.info("Inside changeStatus with parameters {} ", data);
        ResponseMessage<String> responseMessage = new ResponseMessage(null, dashboardService.changeStatus(data));
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/myactivity")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> myActivity(@RequestBody String data) throws IOException {
        log.info("Inside myActivity with parameters {} ", data);
        return new ResponseEntity<>(dashboardService.myActivity(data), HttpStatus.OK);
    }

    @PostMapping("/bulkapprove")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity bulkApprove(@RequestBody String data) throws IOException {
        log.info("Inside bulk approve with parameters {}", data);
        return new ResponseEntity(dashboardService.bulkApprove(data), HttpStatus.OK);
    }

    @PostMapping("/recentcontributions")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity recentContributions(@RequestBody int pageNo) throws IOException {
        log.info("Inside recentContributions with parameters {}", pageNo);
        return new ResponseEntity(dashboardService.recentContributions(pageNo), HttpStatus.OK);
    }

    @PostMapping("/leaderboard")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity leaderboard() throws IOException {
        log.info("Inside leaderboard with no parameters");
        return new ResponseEntity(dashboardService.leaderboard(), HttpStatus.OK);
    }

    @PostMapping("/myprofile")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> myProfile(@RequestBody String empId) throws IOException {
        log.info("Inside myProfile with parameters {}", empId);
        return new ResponseEntity(dashboardService.myProfile(empId), HttpStatus.OK);
    }


    @PostMapping("/contributioncount")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> contributionCount(@RequestBody String empId) throws IOException {
        log.info("Inside contributionCount with parameters {}", empId);
        return new ResponseEntity(dashboardService.contributionCount(empId), HttpStatus.OK);
    }

    @PostMapping("/badgeranking")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity badgeRanking() throws IOException {
        log.info("Inside badgeRanking with no parameters");
        return new ResponseEntity(dashboardService.badgeRanking(), HttpStatus.OK);
    }

    @PostMapping("/vieweventlist")
    @CrossOrigin(origins = "*")
    @ResponseBody
    public ResponseEntity<ResponseMessage> viewEventList(@RequestBody String data) throws IOException {
        log.info("Inside viewEventList with no parameters");
        return new ResponseEntity(dashboardService.viewEventList(data), HttpStatus.OK);
    }
}
