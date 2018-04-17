package com.gamesys.registrationservice.api.exception;


import com.gamesys.registrationservice.api.ErrorEnum;

public abstract class BaseHTTPException extends RuntimeException{

    private String beautyMessage;
    private ErrorEnum apiError;

    public BaseHTTPException(String logMsg, ErrorEnum error){
        super(logMsg);
        this.beautyMessage = error.getMessage();
        this.apiError = error;
    }

    public BaseHTTPException(ErrorEnum error){
        super(error.getMessage());
        this.beautyMessage = error.getMessage();
        this.apiError = error;
    }


    public String getBeautyMessage() {
        return beautyMessage;
    }

    public String getLogMessage(){
        return this.getMessage();
    }

    public ErrorEnum getApiError() {
        return apiError;
    }

}
