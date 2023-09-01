package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.dao.impl.TransactionDaoImpl;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.Transaction;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.Optional;

public class TransactionServiceImpl implements TransactionService {
    private static final Logger logger = LogManager.getLogger();
    private static TransactionServiceImpl instance = new TransactionServiceImpl();
    private TransactionServiceImpl() {

    }

    public static TransactionServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean transferAccount(Double amountTransfer, Account fromAccount, Account toAccount) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match=transactionDao.transferAccount(amountTransfer, fromAccount, toAccount,transaction);
        }catch (DaoException | SQLException e){
            throw new ServiceException(e);
        }
        return match;
    }

    @Override
    public boolean refillAccount(Double amountRefill,Account account) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match= transactionDao.refillAccount(amountRefill,account,transaction);
        } catch (DaoException | SQLException e){
            throw new ServiceException(e);
        }
        return match;
    }

    @Override
    public boolean withdrawalsAccount(Double amountRefill, Account account) throws ServiceException {
        boolean match=false;
        Transaction transaction=new Transaction();
        TransactionDaoImpl transactionDao=TransactionDaoImpl.getInstance();
        try{
            match= transactionDao.withdrawalsAccount(amountRefill,account,transaction);
        } catch (DaoException | SQLException e){
            throw new ServiceException(e);
        }
        return match;
    }
}
