package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<List<Account>> selectAllAccounts() throws ServiceException;

    Optional<Account> selectAccountById(int id) throws ServiceException;

    boolean findAllAccountsByUserID(int id, List<Account> accounts) throws ServiceException;
    boolean insertAccount(Account account) throws ServiceException;
    boolean deleteAccount(Account account) throws ServiceException;
}
