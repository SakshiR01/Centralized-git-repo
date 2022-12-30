package com.gemini.Contripoint.service.interfaces;

import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface WinnerService {

    Integer addRewards(String winnerDetails) throws IOException;
}
