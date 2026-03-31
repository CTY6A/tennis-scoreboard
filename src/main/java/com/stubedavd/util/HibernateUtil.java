package com.stubedavd.util;

import com.stubedavd.model.entity.Match;
import com.stubedavd.model.entity.Player;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import com.stubedavd.exception.InfrastructureException;

public final class HibernateUtil {

    private static SessionFactory sessionFactory;

    private HibernateUtil() {

    }

    public static void init() {

        if (sessionFactory != null) {
            return;
        }

        try {

            sessionFactory = new Configuration()
                    .configure()
                    .addAnnotatedClass(Player.class)
                    .addAnnotatedClass(Match.class)
                    .buildSessionFactory();
        } catch (Exception e) {
            throw new InfrastructureException("Could not initialize Hibernate SessionFactory");
        }
    }

    public static SessionFactory getSessionFactory() {

        if (sessionFactory == null) {
            init();
        }

        return sessionFactory;
    }

    public static void close() {

        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
}
