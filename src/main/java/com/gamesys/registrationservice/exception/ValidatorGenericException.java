package com.gamesys.registrationservice.exception;


import com.gamesys.registrationservice.api.ErrorEnum;

public class ValidatorGenericException extends RuntimeException{

    protected final String fieldName;
    protected final String beautyMessage;
    protected final ErrorEnum apiError;

    public ValidatorGenericException(String fieldName, ErrorEnum errorCode){
        super(errorCode.getMessage());
        this.fieldName = fieldName;
        this.beautyMessage = errorCode.getMessage();
        this.apiError = errorCode;
    }


    public String getFieldName() {
        return fieldName;
    }

    public String getBeautyMessage() {
        return beautyMessage;
    }

    public ErrorEnum getApiError() {
        return apiError;
    }
}
