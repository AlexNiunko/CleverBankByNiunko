package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.entity.Transaction;
import com.example.cleverbankbyniunko.service.BankCheck;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class BankCheckImpl implements BankCheck {
    private static final Logger logger = LogManager.getLogger();
    public static final String TITLE = "Банковский чек";
    public static final String CHECK = "Чек:";
    public static final String TYPE_TRANSACTION = "Тип транзакции:";
    public static final String SENDERS_BANK = "Банк отправителя:";
    public static final String PAYEES_BANK = "Банк получателя:";
    public static final String SENDERS_ACCOUNT = "Счет отправителя:";
    public static final String PAYEES_ACCOUNT = "Счет получателя:";
    public static final String AMOUNT = "Сумма:";

    private Transaction transaction;

    public BankCheckImpl(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public boolean writeCheck(String appPath) throws IOException {
        boolean result = false;
        String check = createCheck();
        String path = appPath + "check\\" + transaction.getId() + ".txt";
        logger.warn(path);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, true))) {
            writer.write(check);
            result = true;
            logger.warn("Write a check was successfully completed");
        } catch (IOException e) {
            logger.warn("Failed to write a check" + e);
            throw new IOException(e);
        }
        return result;
    }


    private String createCheck() {
        StringBuilder builder = new StringBuilder();
        char[] array = new char[(40 - TITLE.length()) / 2];
        String prefixTitle = String.valueOf(array);
        String postfixTitle = String.valueOf(array);
        String date = transaction.getTransactionTime().toLocalDate().toString();
        String time = transaction.getTransactionTime().toLocalTime().toString().substring(0, 8);
        String line1 = prefixTitle + TITLE + postfixTitle;
        String line2 = String.format("%-25s%s", CHECK.trim(), transaction.getId());
        String line3 = String.format("%-25s%s", date, time);
        String line4 = String.format("%-25s%s",TYPE_TRANSACTION,getTypeTransaction(transaction.getTypeTransaction()));
        String line5 = String.format("%-25s%s", SENDERS_BANK.trim(), transaction.getSenderBank());
        String line6 = String.format("%-25s%s", PAYEES_BANK.trim(), transaction.getPayeesBank());
        String line7 = String.format("%-25s%s", SENDERS_ACCOUNT.trim(), transaction.getFromNumber());
        String line8 = String.format("%-25s%s", PAYEES_ACCOUNT.trim(), transaction.getToNumber());
        String line9 = String.format("%-25s%s", AMOUNT.trim(), transaction.getTransactionAmount());
        builder.append(line1).append("\r\n")
                .append(line2).append("\r\n")
                .append(line3).append("\r\n")
                .append(line4).append("\r\n");
        if (transaction.getTypeTransaction().equals(Transaction.TypeTransaction.TRANSFER)) {
            builder.append(line5).append("\r\n")
                    .append(line6).append("\r\n")
                    .append(line7).append("\r\n")
                    .append(line8).append("\r\n");
        }
        builder.append(line9);
        return builder.toString();
    }

    private String getTypeTransaction(Transaction.TypeTransaction transaction) {
        String result;
        switch (transaction) {
            case REFILL:
                result = "Пополнение";
                break;
            case WITHDRAWALS:
                result = "Снятие";
                break;
            case TRANSFER:
                result = "Перевод";
                break;
            default:
                result = "Неизвестная транзакция";
                break;
        }
        return result;
    }
}
