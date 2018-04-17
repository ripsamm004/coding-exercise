package com.gamesys.registrationservice.service;

import com.gamesys.registrationservice.api.UserDTO;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.persistence.UserRepository;
import com.gamesys.registrationservice.persistence.impl.UserRepositoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class ExclusionServiceImplUTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private ExclusionService exclusionService;

    @Before
    public void setup(){
        exclusionService = new ExclusionServiceImpl();
    }

    @Test
    public void testIfUsersDOBYearIsNot1998ThenUserIsValid() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        boolean expected = exclusionService.validate(userDTO.getDob(), userDTO.getSsn());
        assertTrue(expected);
    }

    @Test
    public void testIfUsersDOBYearIs1998ThenUserIsNotValid() throws Exception {
        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1998-05-26", "ssn110");
        boolean expected = exclusionService.validate(userDTO.getDob(), userDTO.getSsn());
        assertThat(expected, is(false));
    }
}
