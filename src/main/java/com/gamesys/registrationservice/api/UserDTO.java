package com.gamesys.registrationservice.api;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * This class is use for data transfer layer it represents the User state.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserDTO {
    private String username;
    private String password;
    private String dob;
    private String ssn;
}
