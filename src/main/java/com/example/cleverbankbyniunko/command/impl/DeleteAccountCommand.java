package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.PagePath;
import com.example.cleverbankbyniunko.command.Router;
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

public class DeleteAccountCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router=new Router();
        HttpSession session= request.getSession();
        Account account=(Account) session.getAttribute(AttributeName.ACCOUNT);
        User user=(User)session.getAttribute(AttributeName.USER);
        AccountServiceImpl accountService=AccountServiceImpl.getInstance();
        List<Account>accounts=new ArrayList<>();
        try{
            if (accountService.deleteAccount(account) && accountService.findAllAccountsByUserID(user.getId(), accounts)){
                String page= PagePath.USER_PAGE;
                session.setAttribute(AttributeName.ACCOUNT_LIST,accounts);
                router.setPage(page);
                router.setRedirect();
            } else {
                String page=PagePath.ACCOUNT_PAGE;
                router.setPage(page);
            }
        }catch (ServiceException e){
            throw new CommandException(e);
        }
        return router;
    }
}
