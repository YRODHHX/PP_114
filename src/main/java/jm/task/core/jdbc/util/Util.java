package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Util {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydbtest";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "root";
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DIALECT = "org.hibernate.dialect.MySQL5Dialect";
    private static SessionFactory sessionFactory;

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("Conn OK");
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection Failed");
        }
        return conn;
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration config = new Configuration();
                Properties props = new Properties();
                props.put(Environment.DRIVER, DB_DRIVER);
                props.put(Environment.URL, DB_URL);
                props.put(Environment.USER, DB_USERNAME);
                props.put(Environment.PASS, DB_PASSWORD);
                props.put(Environment.DIALECT, DIALECT);
                config.setProperties(props);

                config.addAnnotatedClass(User.class);
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(config.getProperties()).build();
                sessionFactory = config.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                System.out.println("Problem creating session factory");
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
