package com.gamesys.registrationservice.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamesys.registrationservice.RegistrationServiceApplication;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.service.ExclusionService;
import com.gamesys.registrationservice.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = RegistrationServiceApplication.class)
public class RegisterControllerITest {

    @LocalServerPort
    private int port;

    String apiEndPoint = createURLWithPort("/register");

    @MockBean
    private ExclusionService exclusionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mvc;



    @Test
    public void testIfGivenValidUserAndNotInBlackListedThenCreateUserWithResponseUserJson() throws Exception {

        UserDTO userDTO = new UserDTO("username01", "443908Rr", "1986-05-20", "ssn101");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())))
                .andReturn();
    }

    @Test
    public void testIfGivenValidUserAndInBlackListedThenResponseJsonErrorOnCrete() throws Exception {

        UserDTO userDTO = new UserDTO("username02", "443908Rr", "1986-05-21", "ssn102");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(false);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_BLACK_LISTED.getCode())))
                .andReturn();

    }

    @Test
    public void testIfGivenInValidUserNameAndNotBlackListedThenResponseJsonErrorOnCrete() throws Exception {

        UserDTO userDTO = new UserDTO("usern ame01", "443908Rr", "1986-05-22", "ssn103");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT.getCode())))
                .andReturn();
    }

    @Test
    public void testIfGivenInValidPasswordAndNotBlackListedThenResponseJsonErrorOnCrete() throws Exception {

        UserDTO userDTO = new UserDTO("username01", "443908rr", "1986-05-22", "ssn103");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_PASSWORD_NOT_CORRECT.getCode())))
                .andReturn();
    }


    @Test
    public void testIfGivenValidUserAndNotBlackListedAndAlreadyExistThenResponseJsonErrorOnCrete() throws Exception {

        UserDTO userDTO = new UserDTO("username10", "443908Rr", "1986-05-23", "ssn104");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isCreated());

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_ALREADY_EXIST.getCode())))
                .andReturn();
    }


    @Test
    public void testIfGivenValidUserDTOThenResponseJsonErrorOnCrete() throws Exception {

        UserDTO userDTO = new UserDTO("", "", "", "");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_DATA_INVALIDE.getCode())))
                .andReturn();
    }


    @Test
    public void testIfGivenValidUserJsonBodyThenResponseJsonErrorOnCrete() throws Exception {


        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(true);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString("")))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_REQUEST_BODY_INVALIDE.getCode())))
                .andReturn();
    }


    @Test
    public void testGivenUserSSNOnGetUserRequestIfUserExistThenResponseUserJson() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn110");
        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        userService.addUser(generateUser(userDTO));

        mvc.perform(get(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())))
                .andReturn();
    }


    @Test
    public void testGivenUserSSNOnGetUserRequestIfUserNotExistThenResponseErrorJson() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-26", "ssn123");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);
        mvc.perform(get(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();
    }



    @Test
    public void testOnGetAllUserRequestIfUsersExistThenResponseUsersJsonArrayWithSizeMoreThanZero() throws Exception {

        UserDTO userDTO1 = new UserDTO("username50", "443908Rr", "1986-05-26", "ssn550");
        UserDTO userDTO2 = new UserDTO("username51", "443908Rr", "1986-05-27", "ssn551");
        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        userService.addUser(generateUser(userDTO1));
        userService.addUser(generateUser(userDTO2));

        mvc.perform(get(apiEndPoint)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(greaterThan(0))))
                .andReturn();
    }


    @Test
    public void testGivenUserSSNOnUpdateUserRequestIfUserExistThenResponseUserJson() throws Exception {


        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);
        UserDTO userDTO = new UserDTO("username12", "443908Rr", "1986-05-16", "ssn210");
        User user = generateUser(userDTO);
        userService.addUser(user);

        UserDTO updateUserDTO = new UserDTO("username14", "443908Rr", "1986-05-16", "ssn210");


        mvc.perform(put(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username", is(updateUserDTO.getUsername())))
                .andReturn();

        User updatedUser = userService.getUser(updateUserDTO.getSsn());
        assertEquals(updatedUser.getSsn(),updateUserDTO.getSsn());
        assertEquals(updatedUser.getDob().toString(), updateUserDTO.getDob());

    }


    @Test
    public void testGivenUserSSNOnUpdateUserRequestIfUserNotExistThenResponseErrorJson() throws Exception {

        UserDTO userDTO = new UserDTO("username22", "443908Rr", "1986-05-16", "ssn250");

        when(exclusionService.validate(userDTO.getDob(), userDTO.getSsn()))
                .thenReturn(true);

        mvc.perform(put(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();
    }


    @Test
    public void testGivenUserSSNOnDeleteRequestIfUserExistThenResponseSuccess() throws Exception {

        UserDTO userDTO = new UserDTO("username23", "443908Rr", "1986-05-18", "ssn260");

        when(exclusionService.validate(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        User user = generateUser(userDTO);
        userService.addUser(user);


        mvc.perform(delete(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }


    @Test
    public void testGivenUserSSNOnDeleteRequestIfUserNotExistThenResponseErrorJson() throws Exception {

        UserDTO userDTO = new UserDTO("username27", "443908Rr", "1986-05-12", "ssn262");

        mvc.perform(delete(apiEndPoint + "/" + userDTO.getSsn())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is(ErrorEnum.API_ERROR_USER_NOT_FOUND.getCode())))
                .andReturn();
    }



    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private User generateUser(UserDTO userDTO) {
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        return new User(userDTO.getUsername(), userDTO.getPassword(), dateTime, userDTO.getSsn());
    }
}