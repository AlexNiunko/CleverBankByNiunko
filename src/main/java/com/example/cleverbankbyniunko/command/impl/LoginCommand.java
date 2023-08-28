package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.PagePath;
import com.example.cleverbankbyniunko.command.Router;
import com.example.cleverbankbyniunko.entity.AbstractEntity;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

public class LoginCommand implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router=new Router();
        HttpSession session= request.getSession();
        String email=request.getParameter(AttributeName.EMAIL);
        String password=request.getParameter(AttributeName.PASSWORD);
        UserServiceImpl userService=UserServiceImpl.getInstance();
        String page;
        Optional<User>optionalUser;
        try {
            if (userService.authenticate(email,password) && (optionalUser=userService.findUserByEmail(email)).isPresent()){
                    User user=optionalUser.get();
                    session.setAttribute(AttributeName.USER,user);
                    page= PagePath.USER_PAGE;
                    router.setPage(page);
                    router.setRedirect();
            }else {
                page=PagePath.INDEX;
                router.setPage(page);
            }
        } catch (ServiceException e) {
            throw new CommandException(e);
        }
        return router;
    }
}
