package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.repository.interfaces.TechnologyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TechnologyRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(ExperienceRepositoryImp.class);

    @Autowired
    private TechnologyRepository technologyRepository;

    public List<String> getTechnologyList() {
        log.debug("Inside Technology (Repository) with no parameters ");
        return technologyRepository.getTechnologyList();
    }
}
