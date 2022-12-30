package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.model.Winner;
import com.gemini.Contripoint.repository.interfaces.EventRepository;
import com.gemini.Contripoint.repository.interfaces.WinnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

@Repository
public class WinnerRepositoryImpl {

    public static final Logger log = LoggerFactory.getLogger(WinnerRepositoryImpl.class);
    @Lazy
    @Autowired
    private WinnerRepository winnerRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private EmployeeRepositoryImp employeeRepositoryImp;

    public Winner addWinner(Winner winner) {
        return winnerRepository.save(winner);
    }
}
