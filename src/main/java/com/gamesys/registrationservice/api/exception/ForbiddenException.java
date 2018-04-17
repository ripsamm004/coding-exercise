package com.gamesys.registrationservice.api.exception;


import com.gamesys.registrationservice.api.ErrorEnum;

public class ForbiddenException extends BaseHTTPException {

    public ForbiddenException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }

}
