package com.gamesys.registrationservice.persistence.impl;

import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.persistence.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository("userRepository")
public class UserRepositoryImpl implements UserRepository {

    private final Map<String, User> userList = new ConcurrentHashMap<>();

    @Override
    public Optional<User> getUser(String key) {
        return Optional.ofNullable(userList.get(key));
    }

    @Override
    public Optional<User> addUser(String key, User user) {
        return Optional.ofNullable(userList.putIfAbsent(key, user) == null ? user : null);
    }

    @Override
    public Optional<User> replaceUser(String key, User user) {
        return Optional.ofNullable(userList.replace(key, user) != null ? user : null);
    }

    @Override
    public boolean removeUser(String key) {
        return userList.remove(key) != null ? true : false;
    }

    @Override
    public List<User> getAllUser() {
        return new ArrayList<>(userList.values());
    }
}