package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.List;
import java.util.Optional;

public interface AccountService {

    Optional<Account> selectAccountById(int id) throws ServiceException;

    boolean findAllAccountsByUserID(int id, List<Account> accounts) throws ServiceException;
}
