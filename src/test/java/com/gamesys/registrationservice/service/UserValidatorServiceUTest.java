package com.gamesys.registrationservice.service;

import com.gamesys.registrationservice.api.UserDTO;
import com.gamesys.registrationservice.api.exception.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserValidatorServiceUTest {


    private UserValidator userValidator;

    @Before
    public void setup(){
        userValidator = new UserValidator();
    }



    @Test
    public void testIfUserPasswordIsNotValidThenExpectFalse() throws Exception {

        String[] password = {"34sdf3342", "RRR", "003d", "3ddrrr"};

        assertThat(userValidator.validatePassword(password[0]), is(false));
        assertThat(userValidator.validatePassword(password[1]), is(false));
        assertThat(userValidator.validatePassword(password[2]), is(false));
        assertThat(userValidator.validatePassword(password[3]), is(false));

    }

    @Test
    public void testIfUserPasswordIsValidThenExpectTrue() throws Exception {

        String[] password = {"rrr3R", "RRRr$3", "dd3D", "999999Rr"};

        assertThat(userValidator.validatePassword(password[0]), is(true));
        assertThat(userValidator.validatePassword(password[1]), is(true));
        assertThat(userValidator.validatePassword(password[2]), is(true));
        assertThat(userValidator.validatePassword(password[3]), is(true));
    }


    @Test
    public void testIfUserNameIsNotValidThenExpectFalse() throws Exception {

        String[] username = {"ripsamm004-", "testspecialchar £004", "test space", "r 333"};

        assertThat(userValidator.validateUsername(username[0]), is(false));
        assertThat(userValidator.validateUsername(username[1]), is(false));
        assertThat(userValidator.validateUsername(username[2]), is(false));
        assertThat(userValidator.validateUsername(username[3]), is(false));
    }


    @Test
    public void testIfUserDOBIsValidThenExpectTrue() throws Exception {

        String[] dob = {"1917-03-02", "1917-12-02", "1917-02-02", "1917-03-02"};

        assertThat(userValidator.validateDob(dob[0]), is(true));
        assertThat(userValidator.validateDob(dob[1]), is(true));
        assertThat(userValidator.validateDob(dob[2]), is(true));
        assertThat(userValidator.validateDob(dob[3]), is(true));

    }


    @Test
    public void testIfUserDOBIsNotValidThenExpectFalse() throws Exception {

        String[] dob = {"1917-03-02 09:02", "1917-20-12", "02-20-1917", "1917-03-02 12:01:04"};

        assertThat(userValidator.validateDob(dob[0]), is(false));
        assertThat(userValidator.validateDob(dob[1]), is(false));
        assertThat(userValidator.validateDob(dob[2]), is(false));
        assertThat(userValidator.validateDob(dob[3]), is(false));
    }

    @Test(expected = BadRequestException.class)
    public void testIfGivenUserDTONameIsNotValidThenThrowExceptions() throws Exception {
        UserDTO userDTO = new UserDTO("usern ame22", "443908Rr", "1986-05-26", "ssn110");
        userValidator.validateUser(userDTO);
    }

    @Test(expected = BadRequestException.class)
    public void testIfGivenUserDTOPasswordIsNotValidThenThrowExceptions() throws Exception {
        UserDTO userDTO = new UserDTO("username22", "44444", "1986-05-26", "ssn110");
        userValidator.validateUser(userDTO);
    }

    @Test(expected = BadRequestException.class)
    public void testIfGivenUserDTODOBIsNotValidThenThrowExceptions() throws Exception {
        UserDTO userDTO = new UserDTO("username22", "44444rR", "1986-30-02", "ssn110");
        userValidator.validateUser(userDTO);
    }

    @Test(expected = BadRequestException.class)
    public void testIfGivenUserDTOHaveEmptyValuesThenThrowExceptions()
            throws Exception {

        UserDTO userDTO = new UserDTO("", "", "", "");
        userValidator.validateUser(userDTO);
    }

    @Test(expected = BadRequestException.class)
    public void testIfGivenUserDTOHaveNullValuesThenThrowExceptions()
            throws Exception {

        UserDTO userDTO = new UserDTO(null, null, null, null);
        userValidator.validateUser(userDTO);
    }
}
