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

import java.util.ArrayList;
import java.util.List;

public class ShowAccountsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router=new Router();
        HttpSession session= request.getSession();
        User user= (User) session.getAttribute(AttributeName.USER);
        AccountServiceImpl accountService=AccountServiceImpl.getInstance();
        List<Account>accountList = new ArrayList<>();
        try{
            if (accountService.findAllAccountsByUserID(user.getId(),accountList)){
                session.setAttribute(AttributeName.ACCOUNT_LIST,accountList);
                String page= PagePath.USER_ACCOUNTS;
                router.setRedirect();
                router.setPage(page);
            } else {
                String page=PagePath.USER_PAGE;
                router.setPage(page);
                request.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.USER_PAGE_MESSAGE);
            }
        }catch (ServiceException e){
            logger.warn("Failed to show accounts");
            throw new CommandException(e);
        }
        return router;
    }
}
