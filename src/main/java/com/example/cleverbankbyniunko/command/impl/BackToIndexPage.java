package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.PagePath;
import com.example.cleverbankbyniunko.command.Router;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import jakarta.servlet.http.HttpServletRequest;

public class BackToIndexPage implements Command {
    @Override
    public Router execute(HttpServletRequest request) throws CommandException, DaoException {
    Router router=new Router();
    String page= PagePath.INDEX;
    router.setPage(page);
    return router;
    }
}
