package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.Optional;

public interface UserService {
    boolean registration(User user) throws ServiceException;
    boolean authenticate(String email, String password) throws ServiceException;
    Optional<User>findUserByEmail(String email) throws ServiceException;


}
