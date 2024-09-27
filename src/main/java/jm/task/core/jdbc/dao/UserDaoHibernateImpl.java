package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
            "age TINYINT NOT NULL)";
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS users";

    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {
    }

    private void executeTransaction(Consumer<Session> sessionConsumer) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            sessionConsumer.accept(session);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createUsersTable() {
        executeTransaction(session -> session.createNativeQuery(CREATE_USER_TABLE).executeUpdate());
    }

    @Override
    public void dropUsersTable() {
        executeTransaction(session -> session.createNativeQuery(DROP_USER_TABLE).executeUpdate());
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        executeTransaction(session -> session.save(new User(name, lastName, age)));
    }

    @Override
    public void removeUserById(long id) {
        executeTransaction(session -> {
            User delUser = session.get(User.class, id);
            if (delUser != null) {
                session.delete(delUser);
            }
        });
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createQuery("from User", User.class).getResultList();
            session.getTransaction().commit();
            return users;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cleanUsersTable() {
        executeTransaction(session -> session.createNativeQuery("DELETE FROM users").executeUpdate());
    }
}