package com.example.cleverbankbyniunko.dao;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.DaoException;

import java.sql.SQLException;

public interface TransactionDao  {
    boolean refillAccount(Double amount, Account account) throws DaoException, SQLException;
}
