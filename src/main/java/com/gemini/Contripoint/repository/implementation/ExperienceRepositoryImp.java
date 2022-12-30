package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.repository.interfaces.ExperienceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExperienceRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(ExperienceRepositoryImp.class);


    @Autowired
    private ExperienceRepository experienceRepository;

    public List<String> getExperience() {
        log.debug("Inside Experience (Repository) with no parameters ");
        return experienceRepository.getExperience();
    }
}
