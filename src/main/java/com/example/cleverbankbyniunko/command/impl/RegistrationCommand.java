package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.*;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpSession;

public class RegistrationCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
        Router router=new Router();
        jakarta.servlet.http.HttpSession session = request.getSession();
        logger.info("First step");
        String userName=request.getParameter(AttributeName.NAME_REGISTRATION);
        String userSurname=request.getParameter(AttributeName.SURNAME_REGISTRATION);
        String userEmail=request.getParameter(AttributeName.EMAIL_REGISTRATION);
        String userPassword=request.getParameter(AttributeName.PASSWORD_REGISTRATION);
        User user= User.builder().name(userName).surname(userSurname).email(userEmail).password(userPassword).build();
        logger.info("Buid user "+user);
        UserServiceImpl userService=UserServiceImpl.getInstance();
        try{
            if (userService.registration(user)){
                logger.info("Registration was successfully completed");
                String page= PagePath.INDEX;
                session.setAttribute(AttributeName.INDEX_USER_PAGE,PageMessages.REGISTRATION_MESSAGE_PASS);
                router.setPage(page);
                router.setRedirect();
            } else {
                logger.info("Registration failed");
                session.setAttribute(AttributeName.INDEX_USER_PAGE, PageMessages.REGISTRATION_MESSAGE_FAIL);
                String page=PagePath.INDEX;
                router.setPage(page);
            }
        }catch (ServiceException e){
            throw new CommandException(e);
        }
        return router;
    }
}
