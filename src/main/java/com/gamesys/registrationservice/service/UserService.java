package com.gamesys.registrationservice.service;

import com.gamesys.registrationservice.api.ErrorEnum;
import com.gamesys.registrationservice.api.exception.NotFoundException;
import com.gamesys.registrationservice.domain.User;
import com.gamesys.registrationservice.exception.ValidatorUserAlreadyExistException;
import com.gamesys.registrationservice.exception.ValidatorUserBlackListedException;
import com.gamesys.registrationservice.persistence.UserRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;


/**
 * This service is offer User data manipulation task.
 */

@Service
public class UserService
{

    @Inject
    protected UserRepository userRepository;

    @Inject
    protected ExclusionService exclusionService;

    public User getUser(String key) {
         return userRepository.getUser(key).orElseThrow(() -> new NotFoundException("User not found", ErrorEnum.API_ERROR_USER_NOT_FOUND));
    }


    public User addUser(User user) {
        checkBlackListed(user);
        return userRepository.addUser(user.getSsn(), user)
                .orElseThrow(() -> new ValidatorUserAlreadyExistException("SSN", ErrorEnum.API_ERROR_USER_ALREADY_EXIST));
    }

    public User replaceUser(User user) {
        checkBlackListed(user);
        return userRepository.replaceUser(user.getSsn(), user)
                .orElseThrow(() -> new NotFoundException("User not found", ErrorEnum.API_ERROR_USER_NOT_FOUND));
    }

    public void removeUser(String key) {
        if (!userRepository.removeUser(key)) {
            throw new NotFoundException("User not fount", ErrorEnum.API_ERROR_USER_NOT_FOUND);
        }
    }

    public List<User> getAllUser() {
        return userRepository.getAllUser();
    }

    private void checkBlackListed(User user) {
        if (!exclusionService.validate(user.getDob().toString(), user.getSsn())) {
            throw new ValidatorUserBlackListedException("DOB and SSN", ErrorEnum.API_ERROR_USER_BLACK_LISTED);
        }
    }

}