package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.*;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.impl.AccountServiceImpl;
import com.example.cleverbankbyniunko.service.impl.TransactionServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransferAccountCommand implements Command {
    private static final Logger logger = LogManager.getLogger();

    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router = new Router();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(AttributeName.USER);
        Account fromAccount = (Account) session.getAttribute(AttributeName.ACCOUNT);
        String beneficiaryNumber = request.getParameter(AttributeName.BENEFICIARY_ACCOUNT);
        Account toAccount = new Account();
        toAccount.setAccountNumber(beneficiaryNumber);
        BigDecimal value = new BigDecimal(request.getParameter(AttributeName.TRANSFER_AMOUNT)).setScale(2, RoundingMode.DOWN);
        TransactionServiceImpl transactionService = TransactionServiceImpl.getInstance();
        AccountServiceImpl accountService = AccountServiceImpl.getInstance();
        Optional<Account> optionalAccount;
        List<Account> accountList = new ArrayList<>();
        String appPath=request.getServletContext().getRealPath("");
        try {
            if (transactionService.transferAccount(value.doubleValue(), fromAccount, toAccount,appPath)) {
                optionalAccount = accountService.selectAccountById(fromAccount.getId());
                Account newAccount;
                if (optionalAccount.isPresent() && accountService.findAllAccountsByUserID(user.getId(), accountList)) {
                    newAccount = optionalAccount.get();
                    session.setAttribute(AttributeName.ACCOUNT, newAccount);
                    session.setAttribute(AttributeName.ACCOUNT_LIST, accountList);
                    session.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.TRANSFER_SUCCESSFUL_COMPLETE_MESSAGE);
                }
                String page = PagePath.ACCOUNT_PAGE;
                router.setPage(page);
                router.setRedirect();
            } else {
                String page = PagePath.TRANSFER_PAGE;
                session.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.TRANSFER_NOT_SUCCESSFUL_COMPLETE_MESSAGE);
                router.setPage(page);
            }
        } catch (ServiceException e) {
            logger.warn("Failed to transfer");
            throw new CommandException(e);
        }
        return router;
    }
}
