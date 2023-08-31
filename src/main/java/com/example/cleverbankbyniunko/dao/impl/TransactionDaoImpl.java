package com.example.cleverbankbyniunko.dao.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.dao.BaseDao;
import com.example.cleverbankbyniunko.dao.TransactionDao;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.Transaction;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class TransactionDaoImpl  implements TransactionDao {
    private static final Logger logger = LogManager.getLogger();
    public static final String SELECT_AMOUNT_CURRENT_ACCOUNT="SELECT amount FROM  clever_bank.accounts WHERE id_account=?";
    public static final String UPDATE_AMOUNT_CURRENT_ACCOUNT="UPDATE clever_bank.accounts SET amount=?  WHERE id_account=?";
    public static final String INSERT_TRANSACTION="INSERT INTO clever_bank.transactions (transaction_time,transaction_amount,id_type) VALUES(?,?,?)";
    private static TransactionDaoImpl instance=new TransactionDaoImpl();

    public static TransactionDaoImpl getInstance() {
        return instance;
    }
    private TransactionDaoImpl() {
    }

    @Override
    public boolean refillAccount(Double amount, Account account) throws DaoException, SQLException {
        boolean match=false;
        Connection connection= ConnectionPool.getInstance().getConnection();
        try(
            PreparedStatement selectAmountBefore= connection.prepareStatement(SELECT_AMOUNT_CURRENT_ACCOUNT);
            PreparedStatement updateAmount= connection.prepareStatement(UPDATE_AMOUNT_CURRENT_ACCOUNT);
            PreparedStatement insertTransaction=connection.prepareStatement(INSERT_TRANSACTION);
            ){
            logger.warn("Hello");
            connection.setAutoCommit(false);
            logger.warn("Hello I am here");
            logger.warn(account.getId());
            selectAmountBefore.setInt(1, account.getId());
            BigDecimal amountBefore;
            logger.warn("Wow , And I am here now");
            try(ResultSet resultSet=selectAmountBefore.executeQuery()){
                logger.warn("Into resultset");
                if (resultSet.next()){
                    amountBefore= BigDecimal.valueOf(resultSet.getDouble(AttributeName.AMOUNT)).setScale(2, RoundingMode.DOWN);
                    logger.warn("This is current amount "+amountBefore);
                }
                else amountBefore=new BigDecimal(0);
            }
            BigDecimal amountAfter=amountBefore.add(new BigDecimal(amount).setScale(2,RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
            updateAmount.setDouble(1,amountAfter.doubleValue());
            updateAmount.setInt(2,account.getId());
            boolean update=updateAmount.executeUpdate()==1;
            if (update){
                logger.warn("Refill account was successfully completed");
                LocalDateTime localDateTime=LocalDateTime.now();
                insertTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                insertTransaction.setDouble(2,amount);
                insertTransaction.setInt(3,Transaction.TypeTransaction.REFILL.ordinal()+1);
                match=insertTransaction.executeUpdate()==1;

            } else {
                logger.warn("Refill account wasn't successfully completed");
            }
            connection.commit();
        }catch (SQLException e){
            connection.rollback();
            logger.warn("Failed to refill current account",e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return match;
    }
}
