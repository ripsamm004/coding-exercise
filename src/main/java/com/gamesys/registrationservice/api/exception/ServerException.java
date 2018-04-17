package com.gamesys.registrationservice.api.exception;


import com.gamesys.registrationservice.api.ErrorEnum;

public class ServerException extends BaseHTTPException {

    public ServerException(String logMessage, ErrorEnum error){
        super(logMessage, error);
    }
}
