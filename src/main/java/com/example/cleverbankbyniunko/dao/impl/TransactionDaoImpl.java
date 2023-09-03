package com.example.cleverbankbyniunko.dao.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
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

public class TransactionDaoImpl implements TransactionDao {
    private static final Logger logger = LogManager.getLogger();
    public static final String SELECT_AMOUNT_CURRENT_ACCOUNT_BY_ID = "SELECT amount FROM  clever_bank.accounts WHERE id_account=?";
    public static final String SELECT_AMOUNT_AND_IDBANK_ID_CURRENT_ACCOUNT_BY_NUMBER = "SELECT amount,id_account,account_bank_id FROM  clever_bank.accounts WHERE account_number=?";
    public static final String UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_ID = "UPDATE clever_bank.accounts SET amount=?  WHERE id_account=?";
    public static final String UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_NUMBER = "UPDATE clever_bank.accounts SET amount=?  WHERE account_number=?";
    public static final String INSERT_REFILL_OR_WITHDRAWALS_TRANSACTION = "INSERT INTO clever_bank.transactions (transaction_time,transaction_amount,id_type) VALUES(?,?,?)";
    public static final String INSERT_TRANSFER_TRANSACTION = "INSERT INTO clever_bank.transactions (transaction_time,transaction_from,transaction_to,transaction_amount,id_type) VALUES(?,?,?,?,?)";
    public static final String SELECT_ID_REFILL_OR_WITHDRAWALS_TRANSACTION = "SELECT id_transaction FROM clever_bank.transactions WHERE (transaction_time=? and transaction_amount=? and id_type=?)";
    public static final String SELECT_ID_TRANSFER_TRANSACTION = "SELECT id_transaction FROM clever_bank.transactions WHERE (transaction_time=? and transaction_from=? and transaction_to=? and transaction_amount=? and id_type=?)";
    private static TransactionDaoImpl instance = new TransactionDaoImpl();

    public static TransactionDaoImpl getInstance() {
        return instance;
    }

    private TransactionDaoImpl() {
    }

    @Override
    public boolean transferAccount(Double amount, Account fromAccount, Account toAccount, Transaction transaction) throws DaoException, SQLException {
        boolean match = false;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement fromAmount = connection.prepareStatement(SELECT_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement toAmount = connection.prepareStatement(SELECT_AMOUNT_AND_IDBANK_ID_CURRENT_ACCOUNT_BY_NUMBER);
             PreparedStatement updateFromAmount = connection.prepareStatement(UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement updateToAmount = connection.prepareStatement(UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_NUMBER);
             PreparedStatement insertTransaction = connection.prepareStatement(INSERT_TRANSFER_TRANSACTION);
             PreparedStatement selectIdTransferTransaction = connection.prepareStatement(SELECT_ID_TRANSFER_TRANSACTION)) {
            connection.setAutoCommit(false);
            fromAmount.setInt(1, fromAccount.getId());
            BigDecimal inputAmount = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            BigDecimal fromBefore, toBefore, toAfter, fromAfter;
            int idToAccount = 0, idTransaction = 0,idBankTo=0;
            try (ResultSet resultSet = fromAmount.executeQuery()) {
                if (resultSet.next()) {
                    fromBefore = resultSet.getBigDecimal(AttributeName.AMOUNT);
                } else
                    fromBefore = new BigDecimal(0);
            }
            boolean ifExistFrom = false;
            if (fromBefore.subtract(inputAmount).doubleValue() >= amount) {
                toAmount.setString(1, toAccount.getAccountNumber());
                try (ResultSet resultSet = toAmount.executeQuery()) {
                    if (resultSet.next()) {
                        toBefore = resultSet.getBigDecimal(AttributeName.AMOUNT);
                        idToAccount = resultSet.getInt(AttributeName.ID_ACCOUNT);
                        idBankTo=resultSet.getInt(AttributeName.ACCOUNT_BANK_ID);
                        ifExistFrom = true;
                    } else toBefore = new BigDecimal(0);
                }
                if (ifExistFrom) {
                    fromAfter = fromBefore.subtract(inputAmount).setScale(2, RoundingMode.DOWN);
                    toAfter = toBefore.add(inputAmount).setScale(2, RoundingMode.DOWN);
                    updateFromAmount.setBigDecimal(1, fromAfter);
                    updateFromAmount.setInt(2, fromAccount.getId());
                    updateToAmount.setBigDecimal(1, toAfter);
                    updateToAmount.setString(2, toAccount.getAccountNumber());
                    boolean update = ((updateFromAmount.executeUpdate() == 1) && (updateToAmount.executeUpdate() == 1));
                    if (update) {
                        logger.warn("Transfer account was successfully completed");
                        LocalDateTime localDateTime = LocalDateTime.now();
                        insertTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                        insertTransaction.setInt(2, fromAccount.getId());
                        insertTransaction.setInt(3, idToAccount);
                        insertTransaction.setDouble(4, amount);
                        insertTransaction.setInt(5, Transaction.TypeTransaction.TRANSFER.ordinal() + 1);
                        match = insertTransaction.executeUpdate() == 1;
                        selectIdTransferTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                        selectIdTransferTransaction.setInt(2, fromAccount.getId());
                        selectIdTransferTransaction.setInt(3, idToAccount);
                        selectIdTransferTransaction.setDouble(4, amount);
                        selectIdTransferTransaction.setInt(5, Transaction.TypeTransaction.TRANSFER.ordinal() + 1);
                        try (ResultSet resultSet = selectIdTransferTransaction.executeQuery()) {
                            if (resultSet.next()) {
                                idTransaction = resultSet.getInt(AttributeName.ID_TRANSACTION);
                            }
                        }
                        transaction.setId(idTransaction);
                        transaction.setTransactionTime(localDateTime);
                        transaction.setFromNumber(fromAccount.getAccountNumber());
                        transaction.setToNumber(toAccount.getAccountNumber());
                        transaction.setTransactionAmount(amount);
                        transaction.setTypeTransaction(Transaction.TypeTransaction.TRANSFER);
                        transaction.setSenderBank(fromAccount.getBank());
                        transaction.setPayeesBank(idBankTo);
                        logger.info(transaction);
                    }
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warn("Failed to transfer current account", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return match;
    }

    @Override
    public boolean withdrawalsAccount(Double amount, Account account, Transaction transaction) throws DaoException, SQLException {
        boolean match = false;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement selectAmountBefore = connection.prepareStatement(SELECT_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement updateAmount = connection.prepareStatement(UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement insertTransaction = connection.prepareStatement(INSERT_REFILL_OR_WITHDRAWALS_TRANSACTION);
             PreparedStatement selectIdTransaction = connection.prepareStatement(SELECT_ID_REFILL_OR_WITHDRAWALS_TRANSACTION)) {
            connection.setAutoCommit(false);
            selectAmountBefore.setInt(1, account.getId());
            BigDecimal inputAmount = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
            BigDecimal amountBefore;
            int idTransaction = 0;
            try (ResultSet resultSet = selectAmountBefore.executeQuery()) {
                if (resultSet.next()) {
                    amountBefore = BigDecimal.valueOf(resultSet.getDouble(AttributeName.AMOUNT)).setScale(2, RoundingMode.DOWN);
                } else amountBefore = new BigDecimal(0);
            }
            if (amountBefore.subtract(inputAmount).doubleValue() >= 0) {
                BigDecimal amountAfter = amountBefore.subtract(inputAmount).setScale(2, RoundingMode.DOWN);
                updateAmount.setDouble(1, amountAfter.doubleValue());
                updateAmount.setInt(2, account.getId());
                boolean update = updateAmount.executeUpdate() == 1;
                if (update) {
                    logger.warn("Withdrawals account was successfully completed");
                    LocalDateTime localDateTime = LocalDateTime.now();
                    insertTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                    insertTransaction.setDouble(2, amount);
                    insertTransaction.setInt(3, Transaction.TypeTransaction.WITHDRAWALS.ordinal() + 1);
                    match = insertTransaction.executeUpdate() == 1;
                    selectIdTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                    selectIdTransaction.setDouble(2, amount);
                    selectIdTransaction.setInt(3, Transaction.TypeTransaction.WITHDRAWALS.ordinal() + 1);
                    try (ResultSet resultSet = selectIdTransaction.executeQuery()) {
                        if (resultSet.next()) {
                            idTransaction = resultSet.getInt(AttributeName.ID_TRANSACTION);
                        }
                    }
                    transaction.setId(idTransaction);
                    transaction.setTransactionTime(localDateTime);
                    transaction.setTransactionAmount(amount);
                    transaction.setTypeTransaction(Transaction.TypeTransaction.WITHDRAWALS);
                    logger.info(transaction);
                }
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warn("Failed to withdrawals current account", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return match;
    }

    @Override
    public boolean refillAccount(Double amount, Account account, Transaction transaction) throws DaoException, SQLException {
        boolean match = false;
        Connection connection = ConnectionPool.getInstance().getConnection();
        try (PreparedStatement selectAmountBefore = connection.prepareStatement(SELECT_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement updateAmount = connection.prepareStatement(UPDATE_AMOUNT_CURRENT_ACCOUNT_BY_ID);
             PreparedStatement insertTransaction = connection.prepareStatement(INSERT_REFILL_OR_WITHDRAWALS_TRANSACTION);
             PreparedStatement selectIdTransaction = connection.prepareStatement(SELECT_ID_REFILL_OR_WITHDRAWALS_TRANSACTION)) {
            connection.setAutoCommit(false);
            selectAmountBefore.setInt(1, account.getId());
            BigDecimal amountBefore;
            int idTransaction = 0;
            try (ResultSet resultSet = selectAmountBefore.executeQuery()) {
                if (resultSet.next()) {
                    amountBefore = BigDecimal.valueOf(resultSet.getDouble(AttributeName.AMOUNT)).setScale(2, RoundingMode.DOWN);
                } else amountBefore = new BigDecimal(0);
            }
            BigDecimal amountAfter = amountBefore.add(new BigDecimal(amount).setScale(2, RoundingMode.DOWN)).setScale(2, RoundingMode.DOWN);
            updateAmount.setDouble(1, amountAfter.doubleValue());
            updateAmount.setInt(2, account.getId());
            boolean update = updateAmount.executeUpdate() == 1;
            if (update) {
                logger.warn("Refill account was successfully completed");
                LocalDateTime localDateTime = LocalDateTime.now();
                insertTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                insertTransaction.setDouble(2, amount);
                insertTransaction.setInt(3, Transaction.TypeTransaction.REFILL.ordinal() + 1);
                match = insertTransaction.executeUpdate() == 1;
                selectIdTransaction.setTimestamp(1, Timestamp.valueOf(localDateTime));
                selectIdTransaction.setDouble(2, amount);
                selectIdTransaction.setInt(3, Transaction.TypeTransaction.REFILL.ordinal() + 1);
                try (ResultSet resultSet = selectIdTransaction.executeQuery()) {
                    if (resultSet.next()) {
                        idTransaction = resultSet.getInt(AttributeName.ID_TRANSACTION);
                    }
                }
                transaction.setId(idTransaction);
                transaction.setTransactionTime(localDateTime);
                transaction.setTransactionAmount(amount);
                transaction.setTypeTransaction(Transaction.TypeTransaction.REFILL);
                logger.info(transaction);
            } else {
                logger.warn("Refill account wasn't successfully completed");
            }
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            logger.warn("Failed to refill current account", e);
            throw new DaoException(e);
        } finally {
            connection.setAutoCommit(true);
            connection.close();
        }
        return match;
    }
}
