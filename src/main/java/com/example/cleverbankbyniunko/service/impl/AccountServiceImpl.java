package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.dao.impl.AccountDaoImpl;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.AccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
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
    public Optional<List<Account>> selectAllAccounts() throws ServiceException {
        Optional<List<Account>>optionalAccounts;
        AccountDaoImpl accountDao=AccountDaoImpl.getInstance();
        try{
            optionalAccounts=Optional.ofNullable(accountDao.findAll());
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        return optionalAccounts;
    }

    @Override
    public Optional<Account> selectAccountById(int id) throws ServiceException {
        Optional<Account>optionalAccount;
        AccountDaoImpl accountDao=AccountDaoImpl.getInstance();
        try{
           optionalAccount=accountDao.selectAccountById(id);
        }catch (DaoException e){
            logger.warn("Faled to select account in Service");
            throw new ServiceException(e);
        }
        logger.warn(optionalAccount.get());
        return optionalAccount;
    }

    @Override
    public boolean findAllAccountsByUserID(int id, List<Account> accounts) throws ServiceException {
        boolean match = false;
        Optional<List<Account>> optionalAccounts;
        List<Account>accountList = new ArrayList<>();
        AccountDaoImpl accountDao = AccountDaoImpl.getInstance();
        try {
            optionalAccounts = accountDao.findAccountsByUserId(id);
            match = optionalAccounts.isPresent();
            if (match) {
                accountList = optionalAccounts.get();
            }
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
        logger.warn("We are in Account service");
        for (Account item:accountList) {
            logger.warn(item.toString());
            accounts.add(item);
        }
        return match;
    }
}
