package com.example.cleverbankbyniunko.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger();
    private static final int POOL_SIZE = 8;
    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_PATH = "data.properties";
    private static ConnectionPool connectionInstance;
    private static Lock locker = new ReentrantLock();
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private BlockingQueue<ProxyConnection> free = new LinkedBlockingQueue<>();
    private BlockingQueue<ProxyConnection> busy = new LinkedBlockingQueue<>();

    static {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            logger.fatal("Failed to register driver {}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConnectionPool()  {
        String url;
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH)) {
            PROPERTIES.load(inputStream);
             url = PROPERTIES.getProperty("db.url");
            for (int i = 0; i < POOL_SIZE; i++) {
                try {
                    free.add(new ProxyConnection(DriverManager.getConnection(url, PROPERTIES)));
                } catch (SQLException e) {
                    logger.error("Failed to create connection {}", e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.fatal("Failed to read configuration file {}", e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
        if (free.size()==0){
            logger.fatal("Failed to create connection pool ");
            throw new ExceptionInInitializerError();
        } else if (free.size()<POOL_SIZE) {
            for (int i = free.size(); i < POOL_SIZE; i++) {
                try {
                    free.add(new ProxyConnection(DriverManager.getConnection(url, PROPERTIES)));
                } catch (SQLException e) {
                    logger.fatal("Failed to create a connection a second time {}", e.getMessage());
                    throw new ExceptionInInitializerError(e);
                }
            }
        }
    }

    public static ConnectionPool getInstance() {
        if (!isInitialized.get()) {
            try {
                locker.lock();
                if (connectionInstance == null) {
                    connectionInstance = new ConnectionPool();
                    isInitialized.set(true);
                }
            } finally {
                locker.unlock();
            }
        }
        return connectionInstance;
    }

    public Connection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = free.take();
            busy.put(connection);
        } catch (InterruptedException e) {
            logger.error("Failed to take connection from pool {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
        return connection;
    }

    public void returnConnection(Connection connection) {
        try {
            if (connection instanceof ProxyConnection) {
                ProxyConnection proxy = (ProxyConnection) connection;
                busy.remove(proxy);
                free.put(proxy);
            } else {
                logger.error("Enemy connection");
            }
        } catch (InterruptedException e) {
            logger.error("Failed to return connection to pool {}", e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void deregisterDriver() {
        DriverManager.getDrivers().asIterator().forEachRemaining(driver -> {
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e) {
                logger.error("Failed to deregister driver {}", e.getMessage());
            }
        });
    }
    public void destroyPool() {
        for (int i = 0; i < this.free.size(); i++) {
            try {
                free.take().reallyClose();
            } catch (InterruptedException e) {
                logger.error("Failed to close connection {}", e.getMessage());
            }
        }
        deregisterDriver();
    }
}
