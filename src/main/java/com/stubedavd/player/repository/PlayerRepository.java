package com.stubedavd.player.repository;

import com.stubedavd.player.entity.Player;
import com.stubedavd.exception.AlreadyExistException;
import com.stubedavd.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class PlayerRepository {

    SessionFactory sessionFactory;

    public PlayerRepository() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Player save(Player player) {

        try (Session session = sessionFactory.openSession()) {

            session.save(player);

            session.beginTransaction().commit();

            return player;
        } catch (ConstraintViolationException e) {

            throw new AlreadyExistException("Player already exists");
        }
    }

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
