package com.stubedavd.match.model.repository.impl;

import com.stubedavd.exception.DatabaseException;
import com.stubedavd.match.model.entity.Match;
import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.match.model.repository.MatchRepository;
import com.stubedavd.util.HibernateUtil;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.SelectionQuery;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;

import java.util.List;

public class MatchRepositoryImpl implements MatchRepository {
    private static final String PLAYER_1 = "player1";
    private static final String PLAYER_2 = "player2";
    private static final String NAME_PARAMETER = "name";
    private static final String ERROR_MATCH_ALREADY_EXISTS = "Match already exists";
    private static final String ERROR_WHILE_SAVING_PLAYER = "Error while saving player";
    private static final String FIND_ALL_HQL = """
            SELECT m FROM Match m
            LEFT JOIN FETCH m.player1
            LEFT JOIN FETCH m.player2
            LEFT JOIN FETCH m.winner
            ORDER BY m.id DESC
            """;
    private static final String FIND_ALL_BY_PLAYER_NAME_HQL = """
            SELECT m FROM Match m
            LEFT JOIN FETCH m.player1
            LEFT JOIN FETCH m.player2
            LEFT JOIN FETCH m.winner
            WHERE m.player1.name = :name OR m.player2.name = :name
            ORDER BY m.id DESC
            """;

    private final SessionFactory sessionFactory;

    public MatchRepositoryImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public long count() {
        Session session = sessionFactory.getCurrentSession();
        HibernateCriteriaBuilder queryBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = queryBuilder.createQuery(Long.class);
        criteriaQuery.select(queryBuilder.count(criteriaQuery.from(Match.class)));
        return session.createQuery(criteriaQuery).getSingleResult();
    }

    public long countByPlayerName(String playerName) {
        Session session = sessionFactory.getCurrentSession();
        HibernateCriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaRoot = criteriaBuilder.createQuery(Long.class);
        Root<Match> root = criteriaRoot.from(Match.class);
        criteriaRoot.where(
                criteriaBuilder.or(
                        criteriaBuilder.equal(root.get(PLAYER_1).get(NAME_PARAMETER), playerName),
                        criteriaBuilder.equal(root.get(PLAYER_2).get(NAME_PARAMETER), playerName)
                )
        );
        criteriaRoot.select(criteriaBuilder.count(root));
        return session.createQuery(criteriaRoot).getSingleResult();
    }

    public List<Match> findAll(int pageNumber, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        SelectionQuery<Match> query = session.createSelectionQuery(FIND_ALL_HQL, Match.class);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    public List<Match> findAllByPlayerName(String name, int pageNumber, int pageSize) {
        Session session = sessionFactory.getCurrentSession();
        SelectionQuery<Match> query = session.createSelectionQuery(FIND_ALL_BY_PLAYER_NAME_HQL, Match.class);
        query.setParameter(NAME_PARAMETER, name);
        query.setFirstResult(pageNumber * pageSize);
        query.setMaxResults(pageSize);
        return query.list();
    }

    public Match save(Match match) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(match);
            return match;
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                String exceptionMessage = e.getMessage();
                if (exceptionMessage != null && exceptionMessage.toLowerCase().contains(NAME_PARAMETER)) {
                    throw new EntityAlreadyExistException(ERROR_MATCH_ALREADY_EXISTS, e);
                }
            }
            throw new DatabaseException(ERROR_WHILE_SAVING_PLAYER + match.getId(), e);
        }
    }
}