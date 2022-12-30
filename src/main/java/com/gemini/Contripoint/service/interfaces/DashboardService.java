package com.gemini.Contripoint.service.interfaces;

import com.gemini.Contripoint.model.Count;
import com.gemini.Contripoint.model.ResponseMessage;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface DashboardService {
    ResponseMessage reviewActivity(String data) throws IOException;

    String changeStatus(String data) throws IOException;

    ResponseMessage myActivity(String data) throws IOException;

    ResponseMessage recentContributions(int pageNo) throws IOException;

    List<ResponseMessage> leaderboard() throws IOException;

    ResponseMessage bulkApprove(String data) throws IOException;

    ResponseMessage myProfile(String empId) throws IOException;

    Count contributionCount(String empId) throws IOException;

    List<ResponseMessage> badgeRanking() throws IOException;

    ResponseMessage viewEventList(String data) throws IOException;


}
