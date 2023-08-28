package com.example.cleverbankbyniunko.dao.impl;

import com.example.cleverbankbyniunko.command.AttributeName;
import com.example.cleverbankbyniunko.dao.BaseDao;
import com.example.cleverbankbyniunko.dao.UserDao;
import com.example.cleverbankbyniunko.entity.AbstractEntity;
import com.example.cleverbankbyniunko.entity.User;
import com.example.cleverbankbyniunko.exception.DaoException;
import com.example.cleverbankbyniunko.pool.ConnectionPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class UserDaoImpl extends BaseDao<User> implements UserDao {
    private static final Logger logger = LogManager.getLogger();
    public static final String SELECT_PASSWORD = "SELECT password from clever_bank.users WHERE email = ?";
    public static final String SELECT_USER_BY_MAIL = "SELECT user_id,name,surname from clever_bank.users WHERE email = ?";

    private static   UserDaoImpl instance=new UserDaoImpl();

    public static UserDaoImpl getInstance() {
        return instance;
    }

    private UserDaoImpl() {

    }

    @Override
    public boolean insert(User user) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(User user) throws DaoException {
        return false;
    }

    @Override
    public List<User> findAll() throws DaoException {
        return null;
    }

    @Override
    public boolean update(User user) throws DaoException {
        return false;
    }

    @Override
    public Optional<User> findUserByEmail(String email) throws DaoException {
        boolean match=false;
        Optional<User>optionalUser;
        try(Connection connection=ConnectionPool.getInstance().getConnection();
            PreparedStatement statement= connection.prepareStatement(SELECT_USER_BY_MAIL);
        ){
            statement.setString(1,email);
            try(ResultSet resultSet= statement.executeQuery()) {
                if (resultSet.next()){
                    User user=User.builder()
                            .name(resultSet.getString(AttributeName.USER_NAME))
                            .surname(resultSet.getString(AttributeName.USER_SURNAME))
                            .email(email)
                            .build();
                    user.setId(resultSet.getLong(AttributeName.USER_ID));
                    optionalUser=Optional.of(user);
                } else {
                    optionalUser=Optional.empty();
                }
            }
        }catch (SQLException e){
            logger.warn("Failed to find user by email UserDao");
            throw new DaoException(e);
        }
        return optionalUser;
    }

    @Override
    public boolean authenticate(String email, String password) throws DaoException {
        boolean match=false;
        try(Connection connection= ConnectionPool.getInstance().getConnection();
            PreparedStatement statement= connection.prepareStatement(SELECT_PASSWORD)
        ){
            statement.setString(1,email);
            try(ResultSet resultSet= statement.executeQuery()) {
                String passFromDB;
                if (resultSet.next()){
                    passFromDB=resultSet.getString(1);
                    match=password.equals(passFromDB);
                    logger.warn(passFromDB);
                }
            }
        }catch (SQLException e){
            logger.warn("Failed to find email or password");
            throw  new DaoException(e);
        }
        return match;
    }
}
