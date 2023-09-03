package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.*;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.impl.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateNewAccount implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.USER);
        LocalDate localDate = LocalDate.now();
        int bank = Integer.parseInt(request.getParameter(AttributeName.BANK));
        int currency = Integer.parseInt(request.getParameter(AttributeName.CURRENCY));
        String accountNumber = getNumber();
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setIdOwner(user.getId());
        account.setBank(bank);
        account.setCurrency(currency);
        account.setAmount((double) 0);
        account.setOpeningDate(localDate.toString());
        logger.warn(account);
        AccountServiceImpl accountService = AccountServiceImpl.getInstance();
        List<Account> accountList = new ArrayList<>();
        try {
            if (accountService.insertAccount(account) && accountService.findAllAccountsByUserID(user.getId(), accountList)) {
                session.setAttribute(AttributeName.ACCOUNT_LIST, accountList);
                session.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.CREATE_ACCOUNT_MESSAGE);
                String page = PagePath.USER_PAGE;
                router.setPage(page);
                router.setRedirect();
            } else {
                String page=PagePath.USER_PAGE;
                session.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.FAILED_CREATE_ACCOUNT_MESSAGE);
                router.setPage(page);
            }
        } catch (ServiceException e) {
            logger.warn("Failed to create new account");
        }
        return router;
    }

    private char[] getArray() {
        Random random = new Random();
        char[] array = new char[4];
        for (int i = 0; i < array.length; i++) {
            if (random.nextInt(2) == 0) {
                array[i] = (char) (65 + random.nextInt(26));
            } else {
                array[i] = (char) (48 + random.nextInt(10));
            }
        }
        return array;
    }

    private String getNumber() {
        StringBuilder builder = new StringBuilder();
        String delimetr = " ";
        for (int i = 0; i < 4; i++) {
            builder.append(getArray()).append(delimetr);
        }
        return builder.toString().trim();
    }

}
