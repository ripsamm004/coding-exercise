package com.gamesys.registrationservice.service;

import com.gamesys.registrationservice.api.UserDTO;
import com.gamesys.registrationservice.api.exception.NotFoundException;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.exception.ValidatorUserBlackListedException;
import com.gamesys.registrationservice.persistence.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserServiceUTest {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @InjectMocks
    private UserService userService;

    @Mock
    protected UserRepository userRepository;

    @Mock
    protected ExclusionService exclusionService;

    @Test(expected = NotFoundException.class)
    public void testGivenUserSSNIsNotExistThenGetUserThrowException()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        when(userRepository.getUser(userDTO.getSsn())).thenReturn(Optional.empty());
        userService.getUser(userDTO.getSsn());
        verify(userRepository, times(1)).getUser(anyString());
    }

    @Test
    public void testGivenUserSSNIsExistThenGetUserBySSNReturnUser()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(userRepository.getUser(user.getSsn())).thenReturn(mockUser);
        User expected = userService.getUser(user.getSsn());

        verify(userRepository, times(1)).getUser(anyString());
        assertEquals(expected,mockUser.get());
    }

    @Test
    public void testGivenUserNotExistAndNotInBlackListedThenAddUserSuccess()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(userRepository.addUser(user.getSsn(), user)).thenReturn(mockUser);

        User expected = userService.addUser(user);

        verify(exclusionService, times(1)).validate(user.getDob().toString(), user.getSsn());
        verify(userRepository, times(1)).addUser(user.getSsn(), user);
        assertEquals(mockUser.get(),expected);
    }


    @Test(expected = ValidatorUserBlackListedException.class)
    public void testGivenUserNotExistAndInBlackListedThenAddUserFail()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        when(userRepository.addUser(user.getSsn(), user)).thenReturn(mockUser);

        userService.addUser(user);
    }


    @Test
    public void testGivenUserExistAndNotInBlackListedThenUpdateUserSuccess()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(userRepository.replaceUser(user.getSsn(), user)).thenReturn(mockUser);

        User expected = userService.replaceUser(user);

        verify(exclusionService, times(1)).validate(user.getDob().toString(), user.getSsn());
        verify(userRepository, times(1)).replaceUser(user.getSsn(), user);
        assertEquals(mockUser.get(),expected);
    }


    @Test(expected = ValidatorUserBlackListedException.class)
    public void testGivenUserExistAndInBlackListedThenUpdateUserThrowException()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        when(userRepository.replaceUser(user.getSsn(), user)).thenReturn(mockUser);

        userService.replaceUser(user);
    }


    @Test(expected = NotFoundException.class)
    public void testGivenUserNotExistAndNotInBlackListedThenUpdateUserThrowException()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        Optional<User> mockUser = Optional.of(user);

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        when(userRepository.getUser(userDTO.getSsn())).thenReturn(Optional.empty());
        userService.replaceUser(user);
    }


    @Test(expected = NotFoundException.class)
    public void testGivenUserSSNIfTheUserNotExistThenDeleteUserThrowException()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        when(userRepository.removeUser(user.getSsn())).thenReturn(false);

        userService.removeUser(user.getSsn());
    }

    @Test
    public void testGivenUserSSNIfTheUserIsExistThenDeleteUserSuccess()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        when(userRepository.removeUser(user.getSsn())).thenReturn(true);

        userService.removeUser(user.getSsn());

        verify(userRepository, times(1)).removeUser(user.getSsn());
    }


    @Test
    public void testIfUsersAreExistThenGetAllUserReturnUserListWithSizeMoreThanZero()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user1 = new User("username22", "443908Rr", dateTime, "ssn110");
        User user2 = new User("username23", "443908Rr", dateTime, "ssn111");
        User user3 = new User("username24", "443908Rr", dateTime, "ssn112");

        List<User> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);
        userList.add(user3);

        when(userRepository.getAllUser()).thenReturn(userList);

        List<User> userListExpected = userService.getAllUser();

        verify(userRepository, times(1)).getAllUser();
        assertThat(userListExpected.size(), is(3));

    }

    @Test
    public void testIfUserNotBlackListedThenReturnNothing() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user1 = new User("username22", "443908Rr", dateTime, "ssn110");
        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        Method method = UserService.class.getDeclaredMethod("checkBlackListed", User.class);
        method.setAccessible(true);
        method.invoke(userService, user1);
    }


    /**
     * Reflection method invocation throws InvocationTargetException exception if the method call throws exceptions
     * Where method.invoke(userService, user1) return the ValidatorUserBlackListedException so we expect InvocationTargetException
     * @throws Exception
     */

    @Test(expected = InvocationTargetException.class)
    public void testIfUserIsBlackListedThenThrowException() throws Exception  {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(false);
        Method method = UserService.class.getDeclaredMethod("checkBlackListed", User.class);
        method.setAccessible(true);
        method.invoke(userService, user);
    }

}
