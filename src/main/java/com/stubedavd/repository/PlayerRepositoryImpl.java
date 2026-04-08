package com.stubedavd.repository;

import com.stubedavd.entity.Player;
import com.stubedavd.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {

    SessionFactory sessionFactory;

    public PlayerRepositoryImpl() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    @Override
    public Player save(Player player) {

        try (Session session = sessionFactory.openSession()) {

            session.save(player);

            session.beginTransaction().commit();

            return player;
        }
    }

    @Override
    public List<Player> findAll() {

        try (Session session = sessionFactory.openSession()) {

            List<Player> result = session.createQuery("from Player").list();

            session.beginTransaction().commit();

            return result;
        }
    }

    @Override
    public Optional<Player> findByName(String name) {

        try (Session session = sessionFactory.openSession()) {

            Player result = (Player) session.createQuery("from Player where name = :name")
                    .setParameter("name", name)
                    .uniqueResult();

            session.beginTransaction().commit();

            return Optional.ofNullable(result);
        }
    }
}
