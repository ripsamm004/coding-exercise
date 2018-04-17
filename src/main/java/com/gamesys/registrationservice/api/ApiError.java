package com.gamesys.registrationservice.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ApiError {
    private String code;
    private String message;
}



