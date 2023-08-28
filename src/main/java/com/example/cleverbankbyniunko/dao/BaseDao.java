package com.example.cleverbankbyniunko.dao;

import com.example.cleverbankbyniunko.entity.AbstractEntity;
import com.example.cleverbankbyniunko.exception.DaoException;

import java.util.List;

public abstract class BaseDao<T extends AbstractEntity> {
    public abstract boolean insert(T t) throws DaoException;
    public abstract boolean delete(T t) throws DaoException;
    public abstract List<T> findAll( )throws DaoException;
    public abstract boolean update(T t) throws DaoException;
}
