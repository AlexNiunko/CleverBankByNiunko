package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.PagePath;
import com.example.cleverbankbyniunko.command.Router;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class GoToCreateNewAccount implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router=new Router();
        HttpSession session= request.getSession();
        session.setAttribute(AttributeName.BANKS, Account.Bank.values());
        session.setAttribute(AttributeName.CURRENCIES,Account.Currency.values());
        String page= PagePath.CREAT_ACCOUNT_PAGE;
        router.setPage(page);
        return router;
    }
}
