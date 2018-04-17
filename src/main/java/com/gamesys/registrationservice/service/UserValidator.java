package com.gamesys.registrationservice.service;

import com.gamesys.registrationservice.api.ErrorEnum;
import com.gamesys.registrationservice.api.UserDTO;
import com.gamesys.registrationservice.api.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This service is validate the request userDTO object to the the valid User.
 */

@Service
public class UserValidator
{


    private final String passwordPattern = "((?=.*\\d)(?=.*[A-Z]).{4,100})";
    private final String userNamePattern = "[A-Za-z0-9]+";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public boolean validatePassword(String password)
    {
        Pattern p = Pattern.compile(passwordPattern);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public boolean validateUsername(String username)
    {
        Pattern p = Pattern.compile(userNamePattern);
        Matcher m = p.matcher(username);
        return m.matches();
    }


    public boolean validateDob(String inputDob)
    {
        LocalDate dateTime = null;
        try {
            dateTime = LocalDate.parse(inputDob, formatter);
        }
        catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public void validateUser(UserDTO userDTO){

        if(StringUtils.isEmpty(userDTO.getUsername())
         || StringUtils.isEmpty(userDTO.getPassword())
         || StringUtils.isEmpty(userDTO.getDob())
         || StringUtils.isEmpty(userDTO.getSsn())
        ) throw new BadRequestException("USER DATA INVALID", ErrorEnum.API_ERROR_USER_DATA_INVALIDE);

        if (!validateUsername(userDTO.getUsername()))
            throw new BadRequestException("Username", ErrorEnum.API_ERROR_USER_NAME_NOT_CORRECT);
        if (!validatePassword(userDTO.getPassword()))
            throw new BadRequestException("Password", ErrorEnum.API_ERROR_USER_PASSWORD_NOT_CORRECT);
        if (!validateDob(userDTO.getDob()))
            throw new BadRequestException("Dob parse", ErrorEnum.API_ERROR_USER_DOB_NOT_CORRECT);

    }
}