package com.gamesys.registrationservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesys.registrationservice.RegistrationServiceApplication;
import com.gamesys.registrationservice.api.exception.*;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.exception.ValidatorUserAlreadyExistException;
import com.gamesys.registrationservice.exception.ValidatorUserBlackListedException;
import com.gamesys.registrationservice.service.UserService;
import com.gamesys.registrationservice.service.UserValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = RegistrationServiceApplication.class)
public class RegisterControllerUTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserValidator userValidator;

    @Autowired
    private MockMvc mvc;


    String apiEndPoint = "/register";

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Test
    public void testIfGivenUserSSNAndFoundUserThenResponseJsonArrayOfUser()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doReturn(user).when(userService).getUser(anyString());

        mvc.perform(get(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())))
                .andReturn();
        verify(userService, times(1)).getUser(user.getSsn());
    }


    @Test
    public void testIfGivenUserSSNAndUserNotFoundThenResponseJsonError()
            throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");


        doThrow(new NotFoundException("User not found", ErrorEnum.API_ERROR_USER_NOT_FOUND))
                .when(userService).getUser(userDTO.getSsn());


        mvc.perform(get(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();
    }


    @Test
    public void testIfGivenUserDTOIsValidThenCreateUserAndResponseJsonArrayOfUser() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doNothing().when(userValidator).validateUser(any(UserDTO.class));
        doReturn(user).when(userService).addUser(any(User.class));
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any());
        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void testIfGivenUserDTOIsBlackListedThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new ValidatorUserBlackListedException("DOB and SSN", ErrorEnum.API_ERROR_USER_BLACK_LISTED))
                .when(userService).addUser(any(User.class));

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_BLACK_LISTED.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any());
        verify(userService, times(1)).addUser(user);
    }


    @Test
    public void testIfGivenUserDTOIsIncorrectThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("", "", "", "");

        doThrow(new BadRequestException("USER DATA INVALID", ErrorEnum.API_ERROR_USER_DATA_INVALIDE))
                .when(userValidator)
                .validateUser(any(UserDTO.class));

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_DATA_INVALIDE.getCode())))
                .andReturn();
    }


    @Test
    public void testIfGivenUserDTOIsNotIncorrectJsonThenNotCreateAndResponseJsonError() throws Exception {


        doNothing().when(userValidator).validateUser(any(UserDTO.class));
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString("")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_REQUEST_BODY_INVALIDE.getCode())))
                .andReturn();
    }

    @Test
    public void testIfGivenUserDTOIsIncorrectUserNameThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new BadRequestException("Username", ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT))
                .when(userValidator)
                .validateUser(any());
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any());
        verify(userService, times(0)).addUser(user);
    }

    @Test
    public void testIfGivenUserDTOIsIncorrectPasswordThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new BadRequestException("Password", ErrorEnum.API_ERROR_USER_PASSWORD_NOT_CORRECT))
                .when(userValidator)
                .validateUser(any(UserDTO.class));


        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_PASSWORD_NOT_CORRECT.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(0)).addUser(user);
    }


    @Test
    public void testIfGivenUserDTOIsIncorrectDOBThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new BadRequestException("Dob parse", ErrorEnum.API_ERROR_USER_DOB_NOT_CORRECT))
                .when(userValidator)
                .validateUser(any(UserDTO.class));

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_DOB_NOT_CORRECT.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(0)).addUser(user);
    }

    @Test
    public void testIfGivenUserDTOIsAlreadyExistThenNotCreateAndResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new ValidatorUserAlreadyExistException("SSN", ErrorEnum.API_ERROR_USER_ALREADY_EXIST))
                .when(userService).addUser(any(User.class));

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_ALREADY_EXIST.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).addUser(user);
    }


    @Test
    public void testIfGivenUserSSNAndFoundValidUserThenUpdateAndResponseJsonUserData() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        doReturn(user).when(userService).replaceUser(any(User.class));
        mvc.perform(put(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).replaceUser(any());
    }


    @Test
    public void testIfGivenUserSSNAndNotFoundUserThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        doThrow(new NotFoundException("User not found", ErrorEnum.API_ERROR_USER_NOT_FOUND)).when(userService).replaceUser(any(User.class));

        mvc.perform(put(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();

    }


    @Test
    public void testIfGivenUserSSNAndFoundUserButNotValidUserDTOThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("usern ame22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("usern ame22", "443908Rr", dateTime, "ssn110");

        doThrow(new BadRequestException("Username", ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT))
                .when(userValidator)
                .validateUser(any(UserDTO.class));

        mvc.perform(put(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(0)).addUser(any());
    }


    @Test
    public void testIfGivenUserSSNAndFoundUserButNotMatchUseDTOSSNThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");
        String wrongSSN = "invalid_ssn";
        mvc.perform(put(apiEndPoint + "/" + wrongSSN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_SSN_NOT_CORRECT.getCode())))
                .andReturn();

        verify(userValidator, times(0)).validateUser(any(UserDTO.class));
        verify(userService, times(0)).addUser(any());
    }



    @Test
    public void testIfGivenUserSSNAndFoundUserThenDeleteUserAndResponseSuccess() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");

        doNothing().when(userService).removeUser(anyString());
        mvc.perform(delete(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(userService, times(1)).removeUser(userDTO.getSsn());
    }

    @Test
    public void testIfGivenUserSSNAndNotFoundUserThenResponseJsonErrorOnDelete() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");

        doThrow(new NotFoundException("User not found", ErrorEnum.API_ERROR_USER_NOT_FOUND)).when(userService).removeUser(anyString());


        mvc.perform(delete(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();

    }


    @Test
    public void testIfGivenInternalExceptionOnCreateThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        when(userService.addUser(any(User.class))).thenThrow(new NullPointerException());
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.INTERNAL_ERROR.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).addUser(user);
    }


    @Test
    public void testIfGivenForbiddenExceptionOnCreateThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        when(userService.addUser(any(User.class))).thenThrow(new ForbiddenException("TEST FORBIDDEN", ErrorEnum.FORBIDDEN_EXCEPTION));
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.FORBIDDEN_EXCEPTION.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).addUser(user);
    }

    @Test
    public void testIfGivenServerExceptionOnCreateThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        when(userService.addUser(any(User.class))).thenThrow(new ServerException("SERVER EXCEPTION TEST", ErrorEnum.SERVER_EXCEPTION));
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.SERVER_EXCEPTION.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).addUser(user);
    }


    @Test
    public void testIfGivenThrowableExceptionOnCreateThenResponseJsonError() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        User user = new User("username22", "443908Rr", dateTime, "ssn110");

        doThrow(new Error("test throwable")).when(userService).addUser(any(User.class));

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.INTERNAL_ERROR.getCode())))
                .andReturn();

        verify(userValidator, times(1)).validateUser(any(UserDTO.class));
        verify(userService, times(1)).addUser(user);
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}