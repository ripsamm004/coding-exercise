package com.gamesys.registrationservice.api.exception;


import com.gamesys.registrationservice.api.ErrorEnum;

public class BadRequestException extends BaseHTTPException {

    public BadRequestException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }

}
