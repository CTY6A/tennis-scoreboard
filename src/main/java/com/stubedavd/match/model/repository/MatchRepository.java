package com.stubedavd.match.model.repository;

import com.stubedavd.match.model.entity.Match;
import com.stubedavd.exception.EntityAlreadyExistException;
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

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "repository.md" в этом же пакете)

    // TODO: В методе сохранения сущностей `save(Match match)` не происходит вызова отката транзакции.
        // Это может привести к неконсистентности данных и утечке ресурсов (например, блокировкам в БД).
        // Транзакция останется открытой, и соединение может не вернуться в пул.
        // Стоит вызывать откат транзакции при любом исключении.

    // Все "магические строки" лучше вынести в именованные константы

    // TODO: Проблема N+1 запросов в методе выборки матчей.
        //  Методы `findPage` и `findByPlayerName` выполняют HQL-запросы вида `"SELECT * FROM matches ..."`.
        // Сущность `Match` имеет связи `@ManyToOne` с `Player`, поэтому при выполнении такого запроса
        // Hibernate сначала получит список матчей (1 запрос), а затем он будет выполнять по 2 дополнительных `SELECT` запроса
        // для каждого матча, чтобы получить связанных с ним игроков. Если на странице 10 матчей,
        // это приведёт к 21 запросу (если все игроки будут разные) вместо одного.

    // TODO: В методе `save` `catch` блок ловит слишком конкретное исключение `ConstraintViolationException`.
        // Если при сохранении произойдёт другая, но не менее важная ошибка она не будет поймана этим блоком `catch`. Это значит, что для неё не будет выполнен откат транзакции, и система может остаться в несогласованном состоянии.
        // Стоит ловить более общий класс проблем — `PersistenceException`.
        // А также стоит добавить `catch` блок во все остальные методы.

    private final SessionFactory sessionFactory;

    public MatchRepository() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    // Можно дать стандартное название count
    public long findTotalCount() {

        try (Session session = sessionFactory.openSession()) {

            HibernateCriteriaBuilder queryBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Long> criteriaQuery = queryBuilder.createQuery(Long.class);

            criteriaQuery.select(queryBuilder.count(criteriaQuery.from(Match.class)));

            return session.createQuery(criteriaQuery).getSingleResult();
        }
    }

    // Можно дать стандартное название countByPlayerName
    public long findCountByName(String playerName) {

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

    // Можно дать стандартное название findAll
    public List<Match> findPage(int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Match> criteriaQuery = criteriaBuilder.createQuery(Match.class);

            Root<Match> root = criteriaQuery.from(Match.class);

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

            SelectionQuery<Match> query = session.createQuery(criteriaQuery);

            query.setFirstResult(pageNumber * pageSize);

            query.setMaxResults(pageSize);

            return query.list();
        }
    }

    // Можно дать стандартное название findAllByPlayerName
    public List<Match> findByPlayerName(String name, int pageNumber, int pageSize) {

        try (Session session = sessionFactory.openSession()) {

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            CriteriaQuery<Match> criteriaQuery = criteriaBuilder.createQuery(Match.class);

            Root<Match> root = criteriaQuery.from(Match.class);

            criteriaQuery.where(
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("player1").get("name"), name),
                            criteriaBuilder.equal(root.get("player2").get("name"), name)
                    )
            );

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("id")));

            SelectionQuery<Match> query = session.createQuery(criteriaQuery);

            query.setFirstResult(pageNumber * pageSize);

            query.setMaxResults(pageSize);

            return query.list();
        }
    }

    public void save(Match match) {

        try (Session session = sessionFactory.openSession()) {

            // Метод Object save(Object object); помечен как @Deprecated(since = "6.0") вместо него лучше использовать void persist(Object object);
            session.save(match);

            // TODO: Сессия начинает транзакцию и сразу её коммитит. Это не имеет смысла. Создавать транзакцию следует до сохранения сущности, а коммитить после сохранения.
            session.beginTransaction().commit();

        } catch (ConstraintViolationException e) {

            // TODO: При любой ошибке `ConstraintViolationException` выбрасывается исключение new EntityAlreadyExistException("Match already exists").
                // Но `ConstraintViolationException` не всегда означает конфликт уникальности, поэтому стоит выбрасывать более общее исключение,
                // а также добавить в него конструктор, который будет принимать исходное исключение, чтобы сохранить всю цепочку и упростить отладку.
            throw new EntityAlreadyExistException("Match already exists");
        }
    }
}
