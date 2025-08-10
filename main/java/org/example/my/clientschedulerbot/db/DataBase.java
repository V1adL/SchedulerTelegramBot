package org.example.my.clientschedulerbot.db;

import org.example.my.clientschedulerbot.entity.Client;
import org.example.my.clientschedulerbot.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataBase {
    private SessionFactory sessionFactory;

    public DataBase(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public  void saveClient(Client client) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(client);
        session.getTransaction().commit();
        session.close();
    }
    public  void saveUser(User user) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();
    }
    public void updateClient(Client client) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(client);
            session.getTransaction().commit();
        }
    }

    public void deleteClient(Long id) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        Client client = session.get(Client.class, id);
        session.delete(client);
        session.getTransaction().commit();
        session.close();
    }
    public  User getUser(long chatId) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        User user = session.createQuery("select u from User u where u.chatId = :chatId", User.class)
                        .setParameter("chatId", chatId).getSingleResultOrNull();
        session.getTransaction().commit();
        session.close();
        return user;

    }
    public List<Client> getClient(User user) {
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            List<Client> clients = session.createQuery("select c from Client c where c.user = :user",
                    Client.class).setParameter("user", user).getResultList();
            session.getTransaction().commit();
            session.close();
            return clients;
    }
    public List<Client> findClientsByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                            "from Client c where c.appointmentDate between :start and :end and c.reminderSent = false", Client.class)
                    .setParameter("start", start)
                    .setParameter("end", end)
                    .list();
        }
    }



}
