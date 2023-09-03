package com.example.cleverbankbyniunko.service.verifier;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.AccountVerifierException;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

public class AccountVerifier implements Runnable {
    private static final Logger logger = LogManager.getLogger();
    private Map<String,Integer>data;
    private Connection connection;

    public AccountVerifier(Map<String, Integer> data, Connection connection) {
        this.data = data;
        this.connection = connection;
    }

    public static final String UPDATE= "UPDATE clever_bank.accounts SET amount=?  WHERE account_number=?";
    public static final String SELECT_ALL_ACCOUNTS="SELECT * FROM clever_bank.accounts";

    private List<Account> getAllAccounts(Connection connection) throws AccountVerifierException {
        List<Account>accounts=new ArrayList<>();
        Optional<Account> optionalAccount;
        try(PreparedStatement statement=connection.prepareStatement(SELECT_ALL_ACCOUNTS)){
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
                    optionalAccount=Optional.ofNullable(account);
                    if (optionalAccount.isPresent()){
                        accounts.add(optionalAccount.get());
                    }else {
                        logger.warn("Failed to find a account from the DB");
                    }
                }
            }
        }catch (SQLException e){
            logger.warn("Failed to select all accounts");
            throw new AccountVerifierException(e);
        }
        return accounts;
    }

    private boolean verify(Account account) {
        Set<String>numbers=data.keySet();
        return numbers.contains(account.getAccountNumber());
    }

    private boolean increase(Double amount, Account account,Connection connection) throws AccountVerifierException {
        boolean update=false;
        try(PreparedStatement statement= connection.prepareStatement(UPDATE)){
            statement.setDouble(1,amount);
            statement.setString(2, account.getAccountNumber());
         update=statement.executeUpdate()==1;
        }catch (SQLException e){
            logger.warn("Failed to increase amount current Account");
            throw new AccountVerifierException(e);
        }
        return update;
    }

    @SneakyThrows
    @Override
    public void run() {
        try {
            while (true) {
                List<Account> accounts=getAllAccounts(this.connection);
                LocalDateTime localDateTime = java.time.LocalDateTime.now();
                for (Account account:accounts) {
                    if ((verify(account)) &&(localDateTime.getDayOfMonth()==25)){
                        int percent=this.data.get(account.getAccountNumber());
                        BigDecimal value=account.getAmount().divide(new BigDecimal(100)).multiply(new BigDecimal(percent)).setScale(2, RoundingMode.DOWN);
                        BigDecimal result=account.getAmount().add(value).setScale(2,RoundingMode.DOWN);
                        if (increase(result.doubleValue(),account,this.connection)){
                            logger.info("Account number "+account.getAccountNumber()+" was increased");
                        }
                    } else {
                        logger.info("Account number "+account.getAccountNumber()+" wasn't increased");
                    }
                }
                Thread.sleep(3000);
            }
        } catch (AccountVerifierException e) {
            logger.warn("Error Verife");
            throw new CommandException(e);
        }
    }
}
