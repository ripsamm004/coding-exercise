package com.gamesys.registrationservice.exception;

import com.gamesys.registrationservice.api.ErrorEnum;

public class ValidatorUserAlreadyExistException extends ValidatorGenericException{


    public ValidatorUserAlreadyExistException(String fieldName, ErrorEnum errorCode){
        super(fieldName, errorCode);
    }

    @Override
    public String getMessage(){
        return this.fieldName + " user already exist";
    }
}
