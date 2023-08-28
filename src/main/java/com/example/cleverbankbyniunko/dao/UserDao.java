package com.example.cleverbankbyniunko.dao;

import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.DaoException;

import java.util.Optional;

public interface UserDao {
    boolean authenticate(String email,String password) throws DaoException;
    Optional<User> findUserByEmail(String email) throws DaoException;
}
