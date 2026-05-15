package com.stubedavd.player.model.repository.impl;

import com.stubedavd.exception.DatabaseException;
import com.stubedavd.exception.EntityAlreadyExistException;
import com.stubedavd.player.model.entity.Player;
import com.stubedavd.player.model.repository.PlayerRepository;
import jakarta.persistence.PersistenceException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class PlayerRepositoryImpl implements PlayerRepository {

    private static final String FIND_BY_NAME_HQL = """
                FROM Player
                WHERE name = :name
            """;
    private static final String NAME_PARAMETER = "name";
    private static final String NAME_ALREADY_EXISTS = "Player with name already exists";
    private static final String ERROR_WHILE_SAVING_PLAYER = "Error while saving player: ";
    private static final String ERROR_WHILE_FINDING_PLAYER_BY_NAME = "Error while finding player by name: ";

    private final SessionFactory sessionFactory;

    public PlayerRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Player save(Player player) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(player);
            return player;
        } catch (PersistenceException e) {
            Throwable cause = e.getCause();
            if (cause instanceof ConstraintViolationException) {
                String exceptionMessage = e.getMessage();
                if (exceptionMessage != null && exceptionMessage.toLowerCase().contains(NAME_PARAMETER)) {
                    throw new EntityAlreadyExistException(NAME_ALREADY_EXISTS, e);
                }
            }
            throw new DatabaseException(ERROR_WHILE_SAVING_PLAYER + player.getName(), e);
        }
    }

    public Optional<Player> findByName(String name) {
        try {
            Session session = sessionFactory.getCurrentSession();
            return session.createQuery(FIND_BY_NAME_HQL, Player.class)
                    .setParameter(NAME_PARAMETER, name)
                    .uniqueResultOptional();
        } catch (Exception e) {
            throw new DatabaseException(ERROR_WHILE_FINDING_PLAYER_BY_NAME + name, e);
        }
    }
}