package com.example.cleverbankbyniunko.controller.listener;

import com.example.cleverbankbyniunko.pool.ConnectionPool;
import com.example.cleverbankbyniunko.service.reader.YamlReaderImpl;
import com.example.cleverbankbyniunko.service.verifier.AccountVerifier;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.sql.Connection;
import java.util.Map;

@WebListener
public class Listener implements ServletContextListener{

    public Listener() {
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Connection connection= ConnectionPool.getInstance().getConnection();
        YamlReaderImpl yamlReader=new YamlReaderImpl();
        Map<String,Integer> data=yamlReader.read();
        AccountVerifier verifier=new AccountVerifier(data,connection);
        Thread thread=new Thread(verifier);
        thread.start();
        /* This method is called when the servlet context is initialized(when the Web application is deployed). */
    }
}
