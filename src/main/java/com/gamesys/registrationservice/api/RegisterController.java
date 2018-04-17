package com.gamesys.registrationservice.api;

import com.gamesys.registrationservice.api.exception.BadRequestException;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.service.UserService;
import com.gamesys.registrationservice.service.UserValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping("/register")
public class RegisterController {

    @Inject
    protected UserValidator userValidator;

    @Inject
    protected UserService userService;


    @GetMapping()
    public List<UserDTO> showAllUser() {
        log.info("GET : SHOW ALL USER");
        return userService.getAllUser()
                .stream()
                .map(RegisterController::userToDto)
                .collect(Collectors.toList());
    }


    @GetMapping("/{ssn}")
    public UserDTO findUserBySsn(@PathVariable String ssn) {
        log.info("GET : FIND USER BY SSN");
        return userToDto(userService.getUser(ssn));
    }

    @PostMapping
    public ResponseEntity<UserDTO> create(@RequestBody @NotNull UserDTO userDTO) {
        log.info("POST USER DTO : " + userDTO);
        userValidator.validateUser(userDTO);
        User user = dtoToUser(userDTO);
        return new ResponseEntity(userToDto(userService.addUser(user)), HttpStatus.CREATED);
    }

    @PutMapping("/{ssn}")
    public UserDTO replaceUser(@RequestBody UserDTO userDTO, @PathVariable String ssn) {
        log.info("PUT : UPDATE USER DTO : " + userDTO);

        if(StringUtils.isEmpty(ssn) || !ssn.equals(userDTO.getSsn())) {
            throw new BadRequestException(ssn + " {ssn} should be equal to " + userDTO.getSsn(), ErrorEnum.API_ERROR_USER_SSN_NOT_CORRECT );
        }
        userValidator.validateUser(userDTO);
        User user = dtoToUser(userDTO);
        return userToDto(userService.replaceUser(user));
    }

    @DeleteMapping("/{ssn}")
    public void delete(@PathVariable String ssn) {
        log.info("DELETE : USER BY SSN");
        userService.removeUser(ssn);
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static User dtoToUser(UserDTO userDTO) {
        LocalDate dateTime = LocalDate.parse(userDTO.getDob(), formatter);
        return new User(userDTO.getUsername(), userDTO.getPassword(), dateTime, userDTO.getSsn());
    }


    private static UserDTO userToDto(User user) {
        return new UserDTO(user.getUsername(), user.getPassword(), user.getDob().format(formatter), user.getSsn());
    }


}