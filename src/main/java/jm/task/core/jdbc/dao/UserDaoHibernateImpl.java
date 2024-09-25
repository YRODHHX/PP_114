package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {
    private static final String CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS users " +
            "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
            "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
            "age TINYINT NOT NULL)";
    private static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS users";

    private static final SessionFactory sessionFactory = Util.getSessionFactory();

    public UserDaoHibernateImpl() {


    }


    @Override
    public void createUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(CREATE_USER_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }

    }

    @Override
    public void dropUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery(DROP_USER_TABLE).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(new User(name, lastName, age));
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User delUser = session.get(User.class, id);
            if (delUser != null) {
                session.delete(delUser);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }

    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            List<User> users = session.createNativeQuery("from users", User.class).getResultList();
            session.getTransaction().commit();
            return users;
        }
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.createNativeQuery("DELETE FROM users").executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            sessionFactory.getCurrentSession().getTransaction().rollback();
        }
    }
}
