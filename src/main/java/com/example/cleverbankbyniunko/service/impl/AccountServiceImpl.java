package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.dao.impl.AccountDaoImpl;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LogManager.getLogger();
    private static AccountServiceImpl instance = new AccountServiceImpl();

    private AccountServiceImpl() {
    }

    public static AccountServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean findAllAccountsByUserID(long id, List<Account> accounts) throws ServiceException {
        boolean match = false;
        Optional<List<Account>> optionalAccounts;
        AccountDaoImpl accountDao = AccountDaoImpl.getInstance();
        try {
            optionalAccounts = accountDao.findAccountsByUserId(id);
            match = optionalAccounts.isPresent();
            if (match) {
                accounts = optionalAccounts.get();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return match;
    }
}
