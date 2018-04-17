package com.gamesys.registrationservice.api.exception;

import com.gamesys.registrationservice.api.ErrorEnum;

public class NotFoundException extends BaseHTTPException {
    public NotFoundException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }
}
