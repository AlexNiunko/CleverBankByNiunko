package com.example.cleverbankbyniunko.service;

import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.ServiceException;

import java.util.Optional;

public interface TransactionService {
    boolean refillAccount(Double amountRefill,Account account,String appPath) throws ServiceException;
    boolean withdrawalsAccount(Double amountWithdrawals,Account account,String appPath) throws ServiceException;
    boolean transferAccount(Double amountTransfer,Account fromAccount,Account toAccount,String appPath) throws ServiceException;
}
