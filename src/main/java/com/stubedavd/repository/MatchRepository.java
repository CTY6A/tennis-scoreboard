package com.stubedavd.repository;

import com.stubedavd.entity.Match;
import com.stubedavd.exception.AlreadyExistException;
import com.stubedavd.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public class MatchRepository {

    SessionFactory sessionFactory;

    public MatchRepository() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Long findTotalCount() {

        try (Session session = sessionFactory.openSession()) {

            HibernateCriteriaBuilder queryBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaQuery = queryBuilder.createQuery(Long.class);

            criteriaQuery.select(queryBuilder.count(criteriaQuery.from(Match.class)));

            return session.createQuery(criteriaQuery).getSingleResult();
        }
    }

    public Long findCountByName(String playerName) {

        try (Session session = sessionFactory.openSession()) {

            HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaRoot = criteriaBuilder.createQuery(Long.class);

            Root<Match> root = criteriaRoot.from(Match.class);

            criteriaRoot.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("player1").get("name"), playerName),
                            criteriaBuilder.equal(root.get("player2").get("name"), playerName)
                    )
            );

            criteriaRoot.select(criteriaBuilder.count(root));

            return session.createQuery(criteriaRoot).getSingleResult();
        }
    }

    public List<Match> findPage(int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            CriteriaQuery<Match> selectQuery = session.getCriteriaBuilder().createQuery(Match.class);

            selectQuery.from(Match.class);

            SelectionQuery<Match> query = session.createQuery(selectQuery);

            query.setFirstResult(pageNumber * pageSize);

            query.setMaxResults(pageSize);

            return query.list();
        }
    }

    public List<Match> findByPlayerName(String name, int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Match> criteriaRoot = criteriaBuilder.createQuery(Match.class);

            Root<Match> root = criteriaRoot.from(Match.class);

            criteriaRoot.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("player1").get("name"), name),
                            criteriaBuilder.equal(root.get("player2").get("name"), name)
                    )
            );

            SelectionQuery<Match> query = session.createQuery(criteriaRoot);

            query.setFirstResult(pageNumber * pageSize);

            query.setMaxResults(pageSize);

            return query.list();
        }
    }

    public Match save(Match match) {

        try (Session session = sessionFactory.openSession()) {

            session.save(match);

            session.beginTransaction().commit();

            return match;
        } catch (ConstraintViolationException e) {

            throw new AlreadyExistException("Match already exists");
        }
    }
}
