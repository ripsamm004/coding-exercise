package com.gamesys.registrationservice.service;

import org.springframework.stereotype.Service;

/**
 * This is stub , like all dob starting with 1998 is fine.
 */

@Service
public class ExclusionServiceImpl implements ExclusionService {

    @Override
    public boolean validate(String dob, String ssn) {
         return !dob.startsWith("1998");
    }
}
