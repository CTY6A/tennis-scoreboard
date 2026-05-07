package com.stubedavd.player.model.repository;

import com.stubedavd.player.model.entity.Player;
import com.stubedavd.exception.AlreadyExistException;
import com.stubedavd.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class PlayerRepository {

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "repository.md" в этом же пакете)

    // TODO: В методе сохранения сущностей `save(Player player)` не происходит вызова отката транзакции.
        // Это может привести к неконсистентности данных и утечке ресурсов (например, блокировкам в БД).
        // Транзакция останется открытой, и соединение может не вернуться в пул.
        // Стоит вызывать откат транзакции при любом исключении.

    // Ключевые слова в тексте HQL-запроса (`from`, `where`) написаны в нижнем регистре.
        // Хотя это и не влияет на работоспособность, написание ключевых слов SQL/HQL в верхнем регистре (`UPPERCASE`) является общепринятым стандартом.
        // Это значительно улучшает читаемость запросов, так как визуально отделяет синтаксические конструкции языка от имён сущностей и полей.

    // Текст HQL запроса удобнее читать, когда он логично разбит на строки, даже если он короткий.
        // Для визуального разделения запросов на строки лучше использовать текстовые блоки

    // Лучше вынести текст HQL запроса в `private static final` константу и дать ей понятное имя.

    // Название параметра "name" тоже лучше вынести в именованную константу

    // TODO: В методе `save` `catch` блок ловит слишком конкретное исключение `ConstraintViolationException`.
        // Если при сохранении произойдёт другая, но не менее важная ошибка она не будет поймана этим блоком `catch`. Это значит, что для неё не будет выполнен откат транзакции, и система может остаться в несогласованном состоянии.
        // Стоит ловить более общий класс проблем — `PersistenceException`.
        // А также стоит добавить `catch` блок метод `findByName`.

    private final SessionFactory sessionFactory;

    // SessionFactory лучше принимать в конструктор, а не получать в нём из утилитного класса.
    public PlayerRepository() {

        sessionFactory = HibernateUtil.getSessionFactory();
    }

    public Player save(Player player) {

        try (Session session = sessionFactory.openSession()) {

            // Метод Object save(Object object); помечен как @Deprecated(since = "6.0") вместо него лучше использовать void persist(Object object);
            session.save(player);

            // TODO: Сессия начинает транзакцию и сразу её коммитит. Это не имеет смысла. Создавать транзакцию следует до сохранения сущности, а коммитить после сохранения.
            session.beginTransaction().commit();

            return player;
        } catch (ConstraintViolationException e) {

            // TODO: При любой ошибке `ConstraintViolationException` выбрасывается исключение new AlreadyExistException("Player already exists").
                // Но `ConstraintViolationException` не всегда означает конфликт имён, поэтому стоит выбрасывать более общее исключение,
                // а также добавить в него конструктор, который будет принимать исходное исключение, чтобы сохранить всю цепочку и упростить отладку.
            throw new AlreadyExistException("Player already exists");
        }
    }

    public Optional<Player> findByName(String name) {

        try (Session session = sessionFactory.openSession()) {

            // Можно записать так:
//            return session.createQuery("from Player where name = :name", Player.class)
//                    .setParameter("name", name)
//                    .uniqueResultOptional();

            // Метод Query createQuery(String queryString); помечен как @Deprecated, а также @SuppressWarnings("rawtypes"). Вместо него лучше использовать <R> Query<R> createQuery(String queryString, Class<R> resultClass);
            Player result = (Player) session.createQuery("from Player where name = :name")
                    .setParameter("name", name)
                    .uniqueResult();

            // TODO: Сессия начинает транзакцию и сразу её коммитит. Это не имеет смысла. Этот метод можно выполнять без транзакции.
            session.beginTransaction().commit();

            return Optional.ofNullable(result);
        }
    }
}
