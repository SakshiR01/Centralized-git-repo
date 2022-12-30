package com.gemini.Contripoint.repository.implementation;

import com.gemini.Contripoint.repository.interfaces.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class ProfileRepositoryImp {

    public static final Logger log = LoggerFactory.getLogger(ProfileRepositoryImp.class);

    @Autowired
    private ProfileRepository profileRepository;

    public List<String> getProfile() throws IOException {
        return profileRepository.getProfile();            // getting data for profile dropdown
    }
}
