package com.example.cleverbankbyniunko.controller;

import java.io.*;
import java.sql.Connection;
import java.util.Map;

import com.example.cleverbankbyniunko.command.CommandType;
import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.command.Command;
import com.example.cleverbankbyniunko.command.Router;
import com.example.cleverbankbyniunko.exception.CommandException;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import com.example.cleverbankbyniunko.service.reader.YamlReaderImpl;
import com.example.cleverbankbyniunko.service.verifier.AccountVerifier;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@WebServlet(name = "controller", urlPatterns = "/controller")
public class Controller extends HttpServlet {
    private static Logger logger = LogManager.getLogger();


    public void init() {
        ConnectionPool.getInstance();

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        process(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req,resp);
    }


    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            String commandStr = request.getParameter(AttributeName.COMMAND);
            Command command = CommandType.defineCommand(commandStr.toUpperCase());
            Router router=command.execute(request);

            if (router.getType().equals(Router.Type.FORWARD)){
                request.getRequestDispatcher(router.getPage()).forward(request,response);
                logger.debug("Forward type");
            } else if (router.getType().equals(Router.Type.REDIRECT)) {
                response.sendRedirect(router.getPage());
                logger.debug("Redirect type");
            }
        } catch (CommandException | IOException e) {
            logger.error("Command error");
            throw new ServletException(e);
        } catch (DaoException e) {
            throw new RuntimeException(e);
        }
    }
    public void destroy() {
        ConnectionPool.getInstance().destroyPool();
    }

}