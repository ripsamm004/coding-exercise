package com.gamesys.registrationservice.api;

public enum ErrorEnum {


    // SERVER ERRORS
    INTERNAL_ERROR                      ("M0001","WE ARE EXPERIENCING SOME ISSUES, PLEASE TRY LATER"),
    NO_FOUND_EXCEPTION                  ("M0002","NO FOUND EXCEPTION"),
    BAD_REQUEST_EXCEPTION               ("M0003","BAD REQUEST EXCEPTION"),
    FORBIDDEN_EXCEPTION                 ("M0004","FORBIDDEN EXCEPTION"),
    SERVER_EXCEPTION                    ("M0005","SERVER EXCEPTION"),

    // API/CONFIG ERRORS
    API_ERROR_USER_NOT_FOUND            ("A0001","USER NOT FOUND"),
    API_ERROR_USER_BLACK_LISTED         ("A0002","USER BLACK LISTED"),
    API_ERROR_USER_NAME_NOT_CORRECT     ("A0003","USER NAME NOT CORRECT"),
    API_ERROR_USER_PASSWORD_NOT_CORRECT ("A0004","USER PASSWORD NOT CORRECT"),
    API_ERROR_USER_DOB_NOT_CORRECT      ("A0005","DATE FORMAT NOT CORRECT"),
    API_ERROR_USER_REQUEST_PARAM_NOT_MATCH   ("A0006","UPDATE REQUEST PARAM NOT MATCH"),
    API_ERROR_USER_ALREADY_EXIST        ("A0007","USER ALREADY EXIST"),
    API_ERROR_USER_DATA_INVALIDE        ("A0008","USER DATA INVALID"),
    API_ERROR_REQUEST_BODY_INVALIDE     ("A0009","REQUEST BODY INVALID"),
    API_ERROR_USER_SSN_NOT_CORRECT      ("A0010","USER SSN NOT CORRECT")
    ;

    private String code;
    private String message;

    private ErrorEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return code+","+message;
    }

    public static ErrorEnum getByCode(String code) {
        if (code == null) return null;
        for(ErrorEnum ec : values()) {
            if( (ec.code).equalsIgnoreCase(code)){
                return ec;
            }
        }
        return INTERNAL_ERROR;
    }
}
