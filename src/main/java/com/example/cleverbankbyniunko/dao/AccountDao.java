package com.example.cleverbankbyniunko.dao;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.DaoException;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    Optional<List<Account>> findAccountsByUserId(long userId) throws DaoException;
    Optional<Account> selectAccountById(long id) throws DaoException;
}
