package com.example.cleverbankbyniunko.dao;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.Transaction;
import com.example.cleverbankbyniunko.exception.DaoException;

import java.sql.SQLException;

public interface TransactionDao  {
    boolean refillAccount(Double amount, Account account, Transaction transaction) throws DaoException, SQLException;
    boolean withdrawalsAccount(Double amount,Account account,Transaction transaction) throws DaoException, SQLException;
    boolean transferAccount(Double amount,Account fromAccount,Account toAccount,Transaction transaction) throws DaoException,SQLException;
}
