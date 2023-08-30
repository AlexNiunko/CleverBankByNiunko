package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    boolean findAllAccountsByUserID(long id, List<Account> accounts) throws ServiceException;
    Optional<Account> selectAccountById(long id) throws ServiceException;
}
