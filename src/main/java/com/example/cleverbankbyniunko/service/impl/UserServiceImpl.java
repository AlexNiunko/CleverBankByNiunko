package com.example.cleverbankbyniunko.service.impl;

import com.example.cleverbankbyniunko.dao.impl.UserDaoImpl;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.exception.ServiceException;
import com.example.cleverbankbyniunko.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    private static final Logger logger = LogManager.getLogger();
    private static UserServiceImpl instance=new UserServiceImpl();

    public static UserServiceImpl getInstance() {
        return instance;
    }

    @Override
    public boolean registration(User user) throws ServiceException {
        boolean match=false;
        UserDaoImpl userDao=UserDaoImpl.getInstance();
        try{
            match=userDao.insert(user);
        }catch (DaoException e){
            throw new ServiceException(e);
        }
        return match;
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws ServiceException {
        UserDaoImpl userDao=UserDaoImpl.getInstance();
        Optional<User>optionalUser;
        try{
            optionalUser=userDao.findUserByEmail(email);
        }catch (DaoException e){
            logger.warn("Failed to find user by current email");
            throw new ServiceException(e);
        }
        return optionalUser;
    }

    @Override
    public boolean authenticate(String email, String password) throws ServiceException {
        UserDaoImpl userDao=UserDaoImpl.getInstance();
        boolean match=false;
        try{
            match= userDao.authenticate(email, password);
        }catch (DaoException e){
            logger.warn("Failed to authenticate");
            throw  new ServiceException(e);
        }
        return match;
    }
}
