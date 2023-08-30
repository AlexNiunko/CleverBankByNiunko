package com.example.cleverbankbyniunko.command.impl;

import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.PagePath;
import com.example.cleverbankbyniunko.command.Router;
import com.example.cleverbankbyniunko.exception.CommandException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BackToPageUserAccountsCommand implements Command {
    private static final Logger logger = LogManager.getLogger();
    @Override
    public Router execute(HttpServletRequest request) throws CommandException {
        Router router=new Router();
        String page= PagePath.USER_ACCOUNTS;
        router.setPage(page);
        return router;
    }
}
