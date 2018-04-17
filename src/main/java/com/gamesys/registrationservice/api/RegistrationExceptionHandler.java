package com.gamesys.registrationservice.api;

import com.gamesys.registrationservice.api.exception.BadRequestException;
import com.gamesys.registrationservice.api.exception.ForbiddenException;
import com.gamesys.registrationservice.api.exception.NotFoundException;
import com.gamesys.registrationservice.api.exception.ServerException;
import com.gamesys.registrationservice.exception.ValidatorGenericException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class RegistrationExceptionHandler {


    @ExceptionHandler(ValidatorGenericException.class)
    public ResponseEntity<Object> handleValidationException(ValidatorGenericException ex) {
        log.error(ex.getMessage());
        return response(ex.getApiError(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleBadRequestException(BadRequestException ex) {

        log.error(ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(ServerException.class)
    public ResponseEntity<Object> handleServerException(ServerException ex) {

        log.error(ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex){
        log.error(ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex){
        log.error(ex.getLogMessage());
        return response(ex.getApiError(), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleJsonObjectParserException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return response(ErrorEnum.API_ERROR_REQUEST_BODY_INVALIDE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error(ex.getMessage());
        return response(ErrorEnum.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> handleGenericThrowable(Throwable ex) {
        log.error(ex.getMessage());
        return response(ErrorEnum.INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private static ResponseEntity<Object> response(ErrorEnum errorEnum,
                                                   HttpStatus httpStatus) {
        ApiError apiError = new ApiError(errorEnum.getCode(), errorEnum.getMessage());
        return new ResponseEntity<>(apiError, httpStatus);
    }
}
