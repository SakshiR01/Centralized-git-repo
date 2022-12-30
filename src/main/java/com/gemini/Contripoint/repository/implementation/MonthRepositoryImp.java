package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.repository.interfaces.MonthRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MonthRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(MonthRepository.class);

    @Autowired
    private MonthRepository monthRepository;

    public List<String> getMonth() {
        return monthRepository.getMonth();
    }
}
