package com.stubedavd.player.model.repository;

import com.stubedavd.player.model.entity.Player;
import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.exception.DatabaseException;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class PlayerRepository {

    private static final String FIND_BY_NAME_HQL = """
                FROM Player
                WHERE name = :name
            """;
    private static final String NAME_PARAMETER = "name";

    // TODO: Класс использует `sessionFactory.openSession()` для получения сессии. Это ведёт к антипаттерну "Session-per-Operation" ("сессия на операцию")
        // (см. файл "repository.md" в этом же пакете)

    private final SessionFactory sessionFactory;

    public PlayerRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Player save(Player player) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(player);
                transaction.commit();
                return player;
            } catch (Exception e) {
                transaction.rollback();
                throw e;
            }
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                String exceptionMessage = e.getMessage();
                if (exceptionMessage != null && exceptionMessage.toLowerCase().contains("name")) {
                    throw new EntityAlreadyExistException("Player with name '" + player.getName() + "' already exists", e);
                }
            }
            throw new DatabaseException("Error while saving player: " + player.getName(), e);
        }
    }

    public Optional<Player> findByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(FIND_BY_NAME_HQL, Player.class)
                    .setParameter(NAME_PARAMETER, name)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DatabaseException("Error while finding player by name: " + name, e);
        }
    }
}
