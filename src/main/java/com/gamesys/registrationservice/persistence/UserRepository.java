package com.gamesys.registrationservice.persistence;

import com.gamesys.registrationservice.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> getUser(String key);
    Optional<User> addUser(String key, User user);
    Optional<User> replaceUser(String key, User user);
    boolean removeUser(String key);
    List<User> getAllUser();
}