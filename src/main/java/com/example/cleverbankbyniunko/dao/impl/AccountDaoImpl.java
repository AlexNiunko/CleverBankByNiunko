package com.example.cleverbankbyniunko.dao.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.dao.AccountDao;
import com.example.cleverbankbyniunko.dao.BaseDao;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AccountDaoImpl extends BaseDao<Account> implements AccountDao  {

    private static final Logger logger = LogManager.getLogger();
    public static final String SELECT_ACCOUNTS_BY_USERID="SELECT id_account,account_number,account_bank_id,amount,id_currency,opening_date from clever_bank.users WHERE id_owner = ?";
    private static AccountDaoImpl instance=new AccountDaoImpl();

    public static AccountDaoImpl getInstance() {
        return instance;
    }
    private AccountDaoImpl() {
    }

    @Override
    public boolean insert(Account account) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Account account) throws DaoException {
        return false;
    }

    @Override
    public List<Account> findAll() throws DaoException {
        return null;
    }

    @Override
    public boolean update(Account account) throws DaoException {
        return false;
    }

    @Override
    public Optional<List<Account>> findAccountsByUserId(long userId) throws DaoException {
        Optional<List<Account>>optionalAccounts;
        List<Account>accounts=new ArrayList<>();
        try(Connection connection= ConnectionPool.getInstance().getConnection();
            PreparedStatement statement=connection.prepareStatement(SELECT_ACCOUNTS_BY_USERID)){
            statement.setInt(1,(int)userId);
            try(ResultSet resultSet= statement.executeQuery()) {
                while (resultSet.next()){
                    Account account=new Account();
                    account.setId(resultSet.getInt(AttributeName.ID_ACCOUNT));
                    account.setAccountNumber(resultSet.getString(AttributeName.ACCOUNT_NUMBER));
                    account.setIdOwner(resultSet.getInt(AttributeName.ID_OWNER));
                    account.setBank(resultSet.getInt(AttributeName.ACCOUNT_BANK_ID));
                    account.setAmount(resultSet.getDouble(AttributeName.AMOUNT));
                    account.setCurrency(resultSet.getInt(AttributeName.ID_CURRENCY));
                    account.setOpeningDate(resultSet.getString(AttributeName.OPENING_DATE));
                    accounts.add(account);
                }
            }
        } catch (SQLException e){
            logger.warn("Failed to select accounts by ID user");
            throw new DaoException(e);
        }
        if (accounts.size()==0){
            optionalAccounts=Optional.empty();
        }else {
            optionalAccounts=Optional.of(accounts);
        }
        return optionalAccounts;
    }
}
