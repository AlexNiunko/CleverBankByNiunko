package com.example.cleverbankbyniunko.dao.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.dao.AccountDao;
import com.example.cleverbankbyniunko.dao.BaseDao;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class AccountDaoImpl extends BaseDao<Account> implements AccountDao {

    private static final Logger logger = LogManager.getLogger();
    public static final String SELECT_ACCOUNTS_BY_USERID = "SELECT id_account,account_number,id_owner,account_bank_id,amount,id_currency,opening_date from clever_bank.accounts WHERE id_owner = ?";
    public static final String SELECT_ACCOUNT_BY_ACCOUNT_ID = "SELECT account_number,id_owner,account_bank_id,amount,id_currency,opening_date from clever_bank.accounts WHERE id_account = ?";
    public static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts";
    public static final String INSERT_ACCOUNT = "INSERT INTO clever_bank.accounts(account_number,id_owner,account_bank_id,amount,id_currency,opening_date) VALUES(?,?,?,?,?,?) ";
    public static final String DELETE_ACCOUNT_BY_NUMBER = "DELETE FROM clever_bank.accounts WHERE account_number=?";
    public static final String UPDATE_ACCOUNT_BY_NUMBER = "UPDATE clever_bank.accounts SET (account_bank_id=?,amount=?,id_currency=?,opening_date=?) WHERE account_number=?";


    private static AccountDaoImpl instance = new AccountDaoImpl();

    public static AccountDaoImpl getInstance() {
        return instance;
    }

    private AccountDaoImpl() {
    }

    @Override
    public boolean insert(Account account) throws DaoException {
        boolean match = false;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ACCOUNT)) {
            statement.setString(1, account.getAccountNumber());
            statement.setInt(2, (int) account.getIdOwner());
            statement.setInt(3, account.getBank().ordinal() + 1);
            statement.setDouble(4, account.getAmount().doubleValue());
            statement.setInt(5, account.getCurrency().ordinal() + 1);
            statement.setDate(6, Date.valueOf(account.getOpeningDate()));
            statement.execute();
            match = true;
        } catch (SQLException e) {
            logger.warn("Failed to insert an account");
            throw new DaoException(e);
        }
        return match;
    }

    @Override
    public boolean delete(Account account) throws DaoException {
        boolean match = false;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ACCOUNT_BY_NUMBER)) {
            statement.setString(1, account.getAccountNumber());
            statement.execute();
            match = true;
        } catch (SQLException e) {
            logger.warn("Failed to delete account");
            throw new DaoException(e);
        }
        return match;
    }

    @Override
    public List<Account> findAll() throws DaoException {
        List<Account> accounts = new ArrayList<>();
        Optional<Account> optionalAccount;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_ACCOUNTS)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(resultSet.getInt(AttributeName.ID_ACCOUNT));
                    account.setAccountNumber(resultSet.getString(AttributeName.ACCOUNT_NUMBER));
                    account.setIdOwner(resultSet.getInt(AttributeName.ID_OWNER));
                    account.setBank(resultSet.getInt(AttributeName.ACCOUNT_BANK_ID));
                    account.setAmount(resultSet.getDouble(AttributeName.AMOUNT));
                    account.setCurrency(resultSet.getInt(AttributeName.ID_CURRENCY));
                    account.setOpeningDate(resultSet.getString(AttributeName.OPENING_DATE));
                    optionalAccount = Optional.ofNullable(account);
                    if (optionalAccount.isPresent()) {
                        accounts.add(optionalAccount.get());
                    } else {
                        logger.warn("Failed to find a account from the DB");
                    }
                }
            }
        } catch (SQLException e) {
            logger.warn("Failed to select all accounts");
            throw new DaoException(e);
        }
        return accounts;
    }

    @Override
    public boolean update(Account account) throws DaoException {
        boolean match=false;
        try(Connection connection=ConnectionPool.getInstance().getConnection();
        PreparedStatement statement= connection.prepareStatement(UPDATE_ACCOUNT_BY_NUMBER)){
            statement.setInt(1,account.getBank().ordinal()+1);
            statement.setDouble(2,account.getAmount().doubleValue());
            statement.setInt(3,account.getCurrency().ordinal()+1);
            statement.setDate(4, Date.valueOf(account.getOpeningDate()));
            statement.executeUpdate();
            match=true;
        }catch (SQLException e){
            logger.warn("Failed to update account");
            throw new DaoException(e);
        }
        return match;
    }

    @Override
    public Optional<Account> selectAccountById(int id) throws DaoException {
        Optional<Account> optionalAccount;
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNT_BY_ACCOUNT_ID)) {
            statement.setInt(1, (int) id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Account account = new Account();
                    account.setId(id);
                    account.setAccountNumber(resultSet.getString(AttributeName.ACCOUNT_NUMBER));
                    account.setIdOwner(resultSet.getInt(AttributeName.ID_OWNER));
                    account.setBank(resultSet.getInt(AttributeName.ACCOUNT_BANK_ID));
                    account.setAmount(resultSet.getDouble(AttributeName.AMOUNT));
                    account.setCurrency(resultSet.getInt(AttributeName.ID_CURRENCY));
                    account.setOpeningDate(resultSet.getString(AttributeName.OPENING_DATE));
                    optionalAccount = Optional.of(account);
                } else {
                    optionalAccount = Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.warn("Failed to select account from DB");
            throw new DaoException(e);
        }
        return optionalAccount;
    }

    @Override
    public Optional<List<Account>> findAccountsByUserId(int userId) throws DaoException {
        Optional<List<Account>> optionalAccounts;
        List<Account> accounts = new ArrayList<>();
        try (Connection connection = ConnectionPool.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ACCOUNTS_BY_USERID)) {
            statement.setInt(1, (int) userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = new Account();
                    account.setId(Integer.parseInt(resultSet.getString(AttributeName.ID_ACCOUNT)));
                    account.setAccountNumber(resultSet.getString(AttributeName.ACCOUNT_NUMBER));
                    account.setIdOwner(Integer.parseInt(resultSet.getString(AttributeName.ID_OWNER)));
                    account.setBank(resultSet.getInt(AttributeName.ACCOUNT_BANK_ID));
                    account.setAmount(resultSet.getDouble(AttributeName.AMOUNT));
                    account.setCurrency(resultSet.getInt(AttributeName.ID_CURRENCY));
                    account.setOpeningDate(resultSet.getString(AttributeName.OPENING_DATE));
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            logger.warn("Failed to select accounts by ID user");
            throw new DaoException(e);
        }
        if (accounts.size() == 0) {
            optionalAccounts = Optional.empty();
        } else {
            optionalAccounts = Optional.of(accounts);
        }
        return optionalAccounts;
    }
}
