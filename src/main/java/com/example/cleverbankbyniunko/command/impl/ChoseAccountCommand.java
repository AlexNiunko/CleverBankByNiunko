package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.*;
import com.example.cleverbankbyniunko.entity.Account;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.impl.AccountServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class ChoseAccountCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router=new Router();
        HttpSession session= request.getSession();
        String idAccount=request.getParameter(AttributeName.ID_ACCOUNT);
        AccountServiceImpl accountService=AccountServiceImpl.getInstance();
        Account account;
        try{
            Optional<Account>optionalAccount=accountService.selectAccountById(Long.valueOf(idAccount));
            if (optionalAccount.isPresent()){
                account=optionalAccount.get();
                session.setAttribute(AttributeName.ACCOUNT,account);
                String page= PagePath.ACCOUNT_PAGE;
                router.setPage(page);
                router.setRedirect();
                logger.warn("Account with ID "+idAccount+" was succefully found");
            }else {
                String page=PagePath.USER_ACCOUNTS;
                request.setAttribute(AttributeName.MESSAGE_USER_PAGE, PageMessages.USER_ACCOUNTS_MESSAGE);
                router.setPage(page);
            }
        }catch (ServiceException e){
            logger.warn("Failed to chose account");
            throw new CommandException(e);
        }
        return router;
    }
}
