package com.gamesys.registrationservice.persistence.impl;

import com.gamesys.registrationservice.api.UserDTO;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.persistence.UserRepository;
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class UserRepositoryImplUTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private UserRepository userRepository;

    @Before
    public void setup(){
        userRepository = new UserRepositoryImpl();
    }

    @Test
    public void testGetUserIfUserExist() throws Exception {
        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user1 = new User("username22", "443908Rr", dateTime, "ssn110");
        User user2 = new User("username23", "443908Rr", dateTime, "ssn111");
        User user3 = new User("username24", "443908Rr", dateTime, "ssn112");
        userRepository.addUser(user1.getSsn(),user1);
        userRepository.addUser(user2.getSsn(),user2);
        userRepository.addUser(user3.getSsn(),user3);
        String key =user3.getSsn();
        Optional<User> userExpected = userRepository.getUser(key);
        assertTrue(userExpected.isPresent());
    }


    @Test
    public void testGetNoUserIfUserNotExist() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user4 = new User("username25", "443908Rr", dateTime, "ssn113");

        String key =user4.getSsn();
        Optional<User> userExpected = userRepository.getUser(key);
        assertTrue(!userExpected.isPresent());
    }

    @Test
    public void testIfUserNotExistAddUserSuccess() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user5 = new User("username26", "443908Rr", dateTime, "ssn114");

        Optional<User> userExpected = userRepository.addUser(user5.getSsn(),user5);
        assertTrue(userExpected.isPresent());
    }


    @Test
    public void testIfUserExistAddUserFail() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user6 = new User("username27", "443908Rr", dateTime, "ssn115");
        userRepository.addUser(user6.getSsn(),user6);

        Optional<User> userExpected = userRepository.addUser(user6.getSsn(),user6);
        assertFalse(userExpected.isPresent());
    }

    @Test
    public void testIfUserExistUpdateUserSuccess() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user7 = new User("username28", "443908Rr", dateTime, "ssn116");
        userRepository.addUser(user7.getSsn(),user7);

        Optional<User> userExpected = userRepository.replaceUser(user7.getSsn(), user7);
        assertTrue(userExpected.isPresent());
    }


    @Test
    public void testIfUserNotExistUpdateUserFail() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user8 = new User("username29", "443908Rr", dateTime, "ssn117");

        Optional<User> userExpected = userRepository.replaceUser(user8.getSsn(),user8);
        assertFalse(userExpected.isPresent());
    }


    @Test
    public void testIfUserNotExistRemoveUserFail() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user9 = new User("username30", "443908Rr", dateTime, "ssn118");

        boolean expected = userRepository.removeUser(user9.getSsn());
        assertThat(expected,is(false));
    }


    @Test
    public void testIfUserExistRemoveUserSuccess() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user10 = new User("username31", "443908Rr", dateTime, "ssn119");
        userRepository.addUser(user10.getSsn(),user10);

        boolean expected = userRepository.removeUser(user10.getSsn());
        assertThat(expected,is(true));
    }


    @Test
    public void testIfUsersExistGetUserListShouldBeWithSizeMoreThanZero() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user11 = new User("username32", "443908Rr", dateTime, "ssn120");
        User user12 = new User("username33", "443908Rr", dateTime, "ssn121");
        User user13 = new User("username34", "443908Rr", dateTime, "ssn122");
        userRepository.addUser(user11.getSsn(),user11);
        userRepository.addUser(user12.getSsn(),user12);
        userRepository.addUser(user13.getSsn(),user13);

        List<User> users = userRepository.getAllUser();
        assertThat(users.size(),is(greaterThan(2)));
    }

}
