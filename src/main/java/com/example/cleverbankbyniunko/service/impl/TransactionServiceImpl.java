package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.dao.impl.TransactionDaoImpl;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.Transaction;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;

public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LogManager.getLogger();
    private static TransactionServiceImpl instance = new TransactionServiceImpl();
    private TransactionServiceImpl() {

    }

    public static TransactionServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean transferAccount(Double amountTransfer, Account fromAccount, Account toAccount,String appPath) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match=transactionDao.transferAccount(amountTransfer, fromAccount, toAccount,transaction);
            BankCheckImpl bankCheck=new BankCheckImpl(transaction);
            bankCheck.writeCheck(appPath);
        }catch (DaoException | SQLException | IOException  e){
            throw new ServiceException(e);
        }
        return match;
    }

    @Override
    public boolean refillAccount(Double amountRefill,Account account,String appPath) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match= transactionDao.refillAccount(amountRefill,account,transaction);
            BankCheckImpl bankCheck=new BankCheckImpl(transaction);
            bankCheck.writeCheck(appPath);
        } catch (DaoException | SQLException | IOException e){
            throw new ServiceException(e);
        }
        return match;
    }

    @Override
    public boolean withdrawalsAccount(Double amountRefill, Account account,String appPath) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match= transactionDao.withdrawalsAccount(amountRefill,account,transaction);
            BankCheckImpl bankCheck=new BankCheckImpl(transaction);
            bankCheck.writeCheck(appPath);
        } catch (DaoException | SQLException |IOException e){
            throw new ServiceException(e);
        }
        return match;
    }
}
