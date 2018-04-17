package com.gamesys.registrationservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 *  This class represent the User. User can have a
 *  username (alphanumerical, no spaces)
 *  password (at least four characters, at least one upper case character, at least one number)
 *  dob (date of birth in ISO 8601 format)
 *  ssn (Social Security Number (SSN))
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    private String username;
    private String password;
    private LocalDate dob;
    private String ssn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return ssn.equals(user.ssn);
    }

    @Override
    public int hashCode() {
        return ssn.hashCode();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", dob=" + dob +
                ", ssn='" + ssn + '\'' +
                '}';
    }
}