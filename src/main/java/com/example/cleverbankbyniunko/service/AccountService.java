package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.List;

public interface AccountService {
    boolean findAllAccountsByUserID(long id, List<Account> accounts) throws ServiceException;
}
